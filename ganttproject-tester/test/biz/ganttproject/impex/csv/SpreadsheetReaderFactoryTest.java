package biz.ganttproject.impex.csv;

import biz.ganttproject.core.model.task.TaskDefaultColumn;
import biz.ganttproject.core.option.BooleanOption;
import com.google.common.collect.ImmutableSet;
import net.sourceforge.ganttproject.io.CSVOptions;
import net.sourceforge.ganttproject.test.task.TaskTestCase;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SpreadsheetReaderFactoryTest extends TaskTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    TaskDefaultColumn.setLocaleApi(null);
  }

  public void testGetSpreadsheetReaderCSV() throws Exception {
    InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
    List<String> headers = new LinkedList<>();
    headers.add("header1");
    headers.add("header2");

    CSVOptions csvOptions = enableOnly(TaskDefaultColumn.ID.getStub().getID());

    SpreadsheetReader reader = SpreadsheetReaderFactory.Companion.getSpreadsheetReader(inputStream, headers, SpreadsheetFormat.CSV, csvOptions);

    assertEquals(reader instanceof CsvReaderImpl, true);
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
