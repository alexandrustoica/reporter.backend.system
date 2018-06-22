package taskly.system.report

import com.google.cloud.vision.v1.*
import com.google.cloud.vision.v1.Feature.Type
import com.google.protobuf.ByteString
import org.springframework.stereotype.Component

@Component
class ValidatedReport {

    fun filter(report: Report): Report {
        val system = ImageAnnotatorClient.create()
        val feature = Feature.newBuilder().setType(Type.LABEL_DETECTION).setMaxResults(30).build()
        val requests = report.photos
                .map { Image.newBuilder().setContent(ByteString.copyFrom(it.bytes)).build() }
                .map { AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(it).build() }
        val responses = system.batchAnnotateImages(requests)
        val result = responses.responsesList.map {
            when {
                report.type === ReportType.PARKING -> isIllegalParking(it.labelAnnotationsList)
                report.type == ReportType.DUMP -> isIllegalDump(it.labelAnnotationsList)
                else -> isIllegalGraffiti(it.labelAnnotationsList)
            }
        }.fold(false) { acc, item -> acc || item }
        return report.copy(isSpam = !result || report.photos.isEmpty())
    }

    private fun isIllegalGraffiti(labelAnnotationsList: List<EntityAnnotation>): Boolean =
            labelAnnotationsList.any {
                (it.description.contains("graffiti") ||
                        it.description.contains("street art")) && it.score > 0.6
            }

    private fun isIllegalDump(labelAnnotationsList: List<EntityAnnotation>): Boolean =
            labelAnnotationsList.any {
                (it.description.contains("waste") || it.description.contains("litter")) && it.score > 0.5
            }

    private fun isIllegalParking(labelAnnotationsList: List<EntityAnnotation>): Boolean =
            labelAnnotationsList.any {
                it.description.contains("car") && it.score > 0.8
            } && (labelAnnotationsList.none {
                it.description.contains("parking") && it.score > 0.3
            } || labelAnnotationsList.none {
                (it.description.contains("street") || it.description.contains("road")) && it.score > 0.6
            })
}