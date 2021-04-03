/*
GanttProject is an opensource project management tool. License: GPL3
Copyright (C) 2004-2012 Thomas Alexandre, GanttProject Team

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package biz.ganttproject.impex.csv;

import biz.ganttproject.core.option.BooleanOption;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.ResourceDefaultColumn;
import net.sourceforge.ganttproject.io.CSVOptions;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.roles.Role;
import net.sourceforge.ganttproject.roles.RoleManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class to export the project in CSV text format
 *
 * @author athomas
 */
public class ResourceCSVExport extends CSVExport {

  private final HumanResourceManager myHumanResourceManager;
  private final CustomPropertyManager myHumanResourceCustomPropertyManager;
  private final RoleManager myRoleManager;

  public ResourceCSVExport(IGanttProject project, CSVOptions csvOptions) {
    this(project.getHumanResourceManager(), project.getRoleManager(), csvOptions);
  }

  ResourceCSVExport(HumanResourceManager resourceManager, RoleManager roleManager, CSVOptions csvOptions) {
    super(csvOptions);
    myHumanResourceManager = Preconditions.checkNotNull(resourceManager);
    myHumanResourceCustomPropertyManager = Preconditions.checkNotNull(resourceManager.getCustomPropertyManager());
    myRoleManager = Preconditions.checkNotNull(roleManager);
  }


  @Override
  public void save(SpreadsheetWriter writer) throws IOException {
    if (myHumanResourceManager.getResources().size() > 0) {
      writeResources(writer);
    }
  }

  private List<CustomPropertyDefinition> writeResourceHeaders(SpreadsheetWriter writer) throws IOException {
    for (Map.Entry<String, BooleanOption> entry : myCsvOptions.getResourceOptions().entrySet()) {
      ResourceDefaultColumn defaultColumn = ResourceDefaultColumn.find(entry.getKey());
      if (!entry.getValue().isChecked()) {
        continue;
      }
      if (defaultColumn == ResourceDefaultColumn.ROLE_IN_TASK) {
        // There's not too much sense in exporting role in task not in the assignment context.
        continue;
      }
      if (defaultColumn == null) {
        if ("id".equals(entry.getKey())) {
          writer.print(i18n("tableColID"));
        } else {
          writer.print(i18n(entry.getKey()));
        }
      } else {
        writer.print(defaultColumn.getName());
      }
    }
    List<CustomPropertyDefinition> customFieldDefs = myHumanResourceCustomPropertyManager.getDefinitions();
    for (CustomPropertyDefinition nextDef : customFieldDefs) {
      writer.print(nextDef.getName());
    }
    writer.println();
    return customFieldDefs;
  }

  private void writeResources(SpreadsheetWriter writer) throws IOException {
    Set<Role> projectRoles = Sets.newHashSet(myRoleManager.getProjectLevelRoles());
    List<CustomPropertyDefinition> customPropDefs = writeResourceHeaders(writer);
    // parse all resources
    for (HumanResource p : myHumanResourceManager.getResources()) {
      for (Map.Entry<String, BooleanOption> entry : myCsvOptions.getResourceOptions().entrySet()) {
        if (!entry.getValue().isChecked()) {
          continue;
        }
        ResourceDefaultColumn defaultColumn = ResourceDefaultColumn.find(entry.getKey());
        if (defaultColumn == null) {
          if ("id".equals(entry.getKey())) {
            writer.print(p.getId());
            continue;
          }
        } else {
          switch (defaultColumn) {
            case NAME:
              writer.print(p.getName());
              break;
            case EMAIL:
              writer.print(p.getMail());
              break;
            case PHONE:
              writer.print(p.getPhone());
              break;
            case ROLE:
              Role role = p.getRole();
              final String sRoleID;
              if (role == null) {
                sRoleID = "0";
              } else if (projectRoles.contains(role)) {
                sRoleID = role.getName();
              } else {
                sRoleID = role.getPersistentID();
              }
              writer.print(sRoleID);
              break;
            case ROLE_IN_TASK:
              // There's not too much sense in exporting role in task not in the assignment context.
              break;
            case STANDARD_RATE:
              writer.print(p.getStandardPayRate());
              break;
            case TOTAL_COST:
              writer.print(p.getTotalCost());
              break;
            case TOTAL_LOAD:
              writer.print(p.getTotalLoad());
              break;
          }
        }
      }
      CSVExportWriterKt.writeCustomPropertyValues(writer, customPropDefs, p.getCustomProperties());
    }
  }

  public HumanResourceManager getMyHumanResourceManager() {
    return myHumanResourceManager;
  }
}
