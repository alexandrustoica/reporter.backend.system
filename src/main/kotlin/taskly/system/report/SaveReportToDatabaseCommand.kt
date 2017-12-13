package taskly.system.report

import taskly.system.command.Command

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

class SaveReportToDatabaseCommand: Command<Report?, Report?> {

    private val repository: ReportRepository
    private val report: Report

    constructor(repository: ReportRepository, report: Report) {
        this.repository = repository
        this.report = report
    }


}