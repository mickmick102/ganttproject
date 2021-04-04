// Copyright (C) 2017 BarD Software
package biz.ganttproject.impex.csv;

import biz.ganttproject.core.model.task.TaskDefaultColumn;
import biz.ganttproject.core.option.BooleanOption;
import com.google.common.collect.ImmutableSet;
import net.sourceforge.ganttproject.GanttProjectImpl;
import net.sourceforge.ganttproject.io.CSVOptions;
import net.sourceforge.ganttproject.test.task.TaskTestCase;

import java.util.Set;

/**
 * @author dbarashev@bardsoftware.com
 */

public class CSVExportFactoryTest extends TaskTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    TaskDefaultColumn.setLocaleApi(null);
  }

  public void testGetCSVExport() throws Exception {
    CSVOptions csvOptions = enableOnly(TaskDefaultColumn.ID.getStub().getID());
    CSVExport csvExport = CSVExportFactory.INSTANCE.getCVSExport("GanttCSVExport", new GanttProjectImpl(), csvOptions);
    assertTrue(csvExport instanceof GanttCSVExport);
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
