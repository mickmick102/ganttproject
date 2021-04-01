package biz.ganttproject.impex.csv

import net.sourceforge.ganttproject.io.CSVOptions
import org.apache.commons.csv.CSVFormat
import java.io.InputStream

class SpreadsheetReaderFactory {
  companion object {
    fun getSpreadsheetReader(stream: InputStream, headers: List<String>,myFormat: SpreadsheetFormat, myCSVOptions: CSVOptions) : SpreadsheetReader {
      return when (myFormat) {
        SpreadsheetFormat.CSV -> CsvReaderImpl(stream, createCSVFormat(headers, myCSVOptions))
        SpreadsheetFormat.XLS -> XlsReaderImpl(stream, headers)
        else -> throw IllegalArgumentException("Unsupported format: $myFormat")
      }
    }

    private fun createCSVFormat(headers: List<String>, myCSVOptions: CSVOptions): CSVFormat {
      var format = CSVFormat.DEFAULT.withIgnoreEmptyLines(false).withIgnoreSurroundingSpaces(true)
      if (myCSVOptions != null) {
        format = format.withDelimiter(myCSVOptions.sSeparatedChar.get(0)).withQuote(myCSVOptions.sSeparatedTextChar.get(0))
      }
      if (headers != null) {
        format = format.withHeader(*headers.toTypedArray())
      }
      return format
    }
  }
}