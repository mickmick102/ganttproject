// Copyright (C) 2017 BarD Software
package biz.ganttproject.impex.csv;

import biz.ganttproject.core.model.task.TaskDefaultColumn;
import biz.ganttproject.core.option.BooleanOption;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.GanttTask;
import net.sourceforge.ganttproject.io.CSVOptions;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.roles.RoleManagerImpl;
import net.sourceforge.ganttproject.task.CustomColumnsManager;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.test.task.TaskTestCase;
import org.apache.commons.csv.CSVFormat;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static biz.ganttproject.impex.csv.SpreadsheetFormat.CSV;

/**
 * @author dbarashev@bardsoftware.com
 */

public class ResourceCSVExportTest extends TaskTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    TaskDefaultColumn.setLocaleApi(null);
  }

  public void testResourceDefaultColumn() throws Exception {
    HumanResourceManager hrManager = new HumanResourceManager(null, new CustomColumnsManager());
    RoleManager roleManager = new RoleManagerImpl();
    CSVOptions csvOptions = enableOnly("id","0","1","2","3","5","6","7");
    CustomPropertyDefinition prop1 = hrManager.getCustomPropertyManager().createDefinition(
      CustomPropertyManager.PropertyTypeEncoder.encodeFieldType(String.class), "prop1", null);
    CustomPropertyDefinition prop2 = hrManager.getCustomPropertyManager().createDefinition(
      CustomPropertyManager.PropertyTypeEncoder.encodeFieldType(String.class), "prop2", null);
    CustomPropertyDefinition prop3 = hrManager.getCustomPropertyManager().createDefinition(
      CustomPropertyManager.PropertyTypeEncoder.encodeFieldType(String.class), "prop3", null);
    hrManager.create("HR1", 1);
    hrManager.create("HR2", 2);
    hrManager.create("HR3", 3);

    hrManager.getById(1).addCustomProperty(prop3, "1");
    hrManager.getById(2).addCustomProperty(prop2, "2");
    hrManager.getById(3).addCustomProperty(prop1, "3");

    CSVExport exporter = new ResourceCSVExport(hrManager, roleManager, csvOptions);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try (SpreadsheetWriter writer = exporter.createWriter(outputStream, CSV)) {
      exporter.save(writer);
    }
    String[] lines = new String(outputStream.toByteArray(), Charsets.UTF_8.name()).split("\\n");
    assertEquals(4, lines.length);
    assertEquals("tableColID,tableColResourceName,tableColResourceRole,tableColResourceEMail,tableColResourcePhone,tableColResourceRate,tableColResourceCost,tableColResourceLoad,prop1,prop2,prop3", lines[0].trim());
    assertEquals("1,HR1,0,,,0,0,0.0,,,1", lines[1].trim());
    assertEquals("2,HR2,0,,,0,0,0.0,,2,", lines[2].trim());
    assertEquals("3,HR3,0,,,0,0,0.0,3,,", lines[3].trim());

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
