package biz.ganttproject.impex.csv;

import biz.ganttproject.core.model.task.TaskDefaultColumn;
import biz.ganttproject.core.option.BooleanOption;
import com.google.common.collect.ImmutableSet;
import net.sourceforge.ganttproject.io.CSVOptions;
import net.sourceforge.ganttproject.test.task.TaskTestCase;

import java.io.ByteArrayOutputStream;
import java.util.Set;

public class SpreadsheetWriterFactoryTest extends TaskTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    TaskDefaultColumn.setLocaleApi(null);
  }

  public void testGetSpreadsheetWriterCSV() throws Exception {
    CSVOptions csvOptions = enableOnly(TaskDefaultColumn.ID.getStub().getID());

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    SpreadsheetWriter writer = SpreadsheetWriterFactory.Companion.getSpreadsheetWriter(outputStream,SpreadsheetFormat.CSV,csvOptions);

    assertEquals(writer instanceof CsvWriterImpl, true);
  }

  public void testGetSpreadsheetWriterXLS() throws Exception {
    CSVOptions csvOptions = enableOnly(TaskDefaultColumn.ID.getStub().getID());

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    SpreadsheetWriter writer = SpreadsheetWriterFactory.Companion.getSpreadsheetWriter(outputStream,SpreadsheetFormat.XLS,csvOptions);

    assertEquals(writer instanceof XlsWriterImpl, true);
  }

  private static CSVOptions enableOnly(String... fields) {
    CSVOptions csvOptions = new CSVOptions();
    Set fieldSet = ImmutableSet.copyOf(fields);
    for (BooleanOption option : csvOptions.getTaskOptions().values()) {
      if (!fieldSet.contains(option.getID())) {
        option.setValue(false);
      }
    }
    for (BooleanOption option : csvOptions.getResourceOptions().values()) {
      if (!fieldSet.contains(option.getID())) {
        option.setValue(false);
      }
    }
    return csvOptions;
  }
}
