package biz.ganttproject.impex.csv

import com.google.common.base.Preconditions
import net.sourceforge.ganttproject.io.CSVOptions
import org.apache.commons.csv.CSVFormat
import java.io.IOException
import java.io.OutputStream

class SpreadsheetWriterFactory {
  companion object {
    fun getSpreadsheetWriter(stream: OutputStream ,format: SpreadsheetFormat, myCSVOptions: CSVOptions): SpreadsheetWriter? {
      Preconditions.checkNotNull<SpreadsheetFormat>(format)

      return when (format) {
        SpreadsheetFormat.CSV -> getCsvWriter(stream, myCSVOptions)
        SpreadsheetFormat.XLS -> getXlsWriter(stream)
        else -> throw IllegalArgumentException("Unsupported format == $format!")
      }

    }

    @Throws(IOException::class)
    private fun getCsvWriter(stream: OutputStream, myCSVOptions: CSVOptions): SpreadsheetWriter? {
      return CsvWriterImpl(stream, getCSVFormat(myCSVOptions), myCSVOptions.getBomOption().getValue())
    }

    private fun getXlsWriter(stream: OutputStream): SpreadsheetWriter? {
      return XlsWriterImpl(stream)
    }

    private fun getCSVFormat(myCSVOptions: CSVOptions): CSVFormat {
      var format = CSVFormat.DEFAULT.withEscape('\\')
      if (myCSVOptions.sSeparatedChar.length == 1) {
        format = format.withDelimiter(myCSVOptions.sSeparatedChar.get(0))
      }
      if (myCSVOptions.sSeparatedTextChar.length == 1) {
        format = format.withQuote(myCSVOptions.sSeparatedTextChar.get(0))
      }
      return format
    }
  }
}