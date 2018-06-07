package taskly.system.report

import com.google.cloud.vision.v1.*
import com.google.cloud.vision.v1.Feature.Type
import com.google.protobuf.ByteString
import java.util.ArrayList
import java.io.FileInputStream
import com.google.auth.oauth2.GoogleCredentials
import com.google.protobuf.Descriptors


class ValidatedReport(private val report: Report) {

    fun value(): Report =
            markAsSpam(report)

    private fun markAsSpam(report: Report): Report {
        val system = ImageAnnotatorClient.create()
        val feature = Feature.newBuilder().setType(Type.LABEL_DETECTION).setMaxResults(30).build()
        val requests = report.photos
                .map { Image.newBuilder().setContent(ByteString.copyFrom(it.bytes)).build() }
                .map { AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(it).build() }
        val responses = system.batchAnnotateImages(requests)
        val results = responses.responsesList.filter {
            it.labelAnnotationsList.none  { label ->
                label.description.contains("vehicle") && label.score < 0.8 ||
                        label.description.contains("parking") && label.score < 0.3 ||
                                label.description.contains("road|street") && label.score > 0.7
            }
        }
        return report.copy(isSpam = results.isEmpty())
    }
}