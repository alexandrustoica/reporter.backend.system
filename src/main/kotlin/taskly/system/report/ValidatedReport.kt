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
        val feature = Feature.newBuilder().setType(Type.LABEL_DETECTION).build()
        val requests = report.photos
                .map { Image.newBuilder().setContent(ByteString.copyFrom(it.bytes)).build() }
                .map { AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(it).build() }
        val responses = system.batchAnnotateImages(requests)
        val results = responses.responsesList.flatMap {
            it.labelAnnotationsList.filter { label ->
                label.description.contains("parking")
            }
        }
        return report.copy(isSpam = results.isEmpty())
    }
}