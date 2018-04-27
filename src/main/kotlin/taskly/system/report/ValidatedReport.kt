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
        val imageAnnotator = ImageAnnotatorClient.create()
        val img = Image.newBuilder()
                .setContent(ByteString.copyFrom(report.photos.get(0).bytes))
                .build()
        val feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build()
        val request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build()
        val responses = imageAnnotator.batchAnnotateImages(listOf(request))
        print(responses)
        responses.responsesList.forEach { response ->
            println(response.labelAnnotationsList.filter { it.description == "yellow" })
        }
        return report.copy(isSpam = true)
    }
}