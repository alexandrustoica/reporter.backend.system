package taskly.system.report

data class ReportWithOwner (
        val report: Report,
        val ownerUsername: String) {
    constructor(): this(Report(), "none")
}