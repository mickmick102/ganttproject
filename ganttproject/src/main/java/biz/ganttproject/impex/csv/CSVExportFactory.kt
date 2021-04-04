package biz.ganttproject.impex.csv

import net.sourceforge.ganttproject.IGanttProject
import net.sourceforge.ganttproject.io.CSVOptions

object CSVExportFactory {

  fun getCVSExport(typeCSVExport: String, project: IGanttProject, csvOptions: CSVOptions) = when (typeCSVExport) {
    "GanttCSVExport" -> GanttCSVExport(project, csvOptions)
    "ResourceCSVExport" -> ResourceCSVExport(project, csvOptions)
    else -> throw IllegalArgumentException("Unsupported format: $typeCSVExport")
  }

}