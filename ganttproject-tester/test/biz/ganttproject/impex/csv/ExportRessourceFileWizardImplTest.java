package biz.ganttproject.impex.csv;

import biz.ganttproject.core.model.task.TaskDefaultColumn;
import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.GanttProjectImpl;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.export.ExportRessourceFileWizardImpl;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.io.CSVOptions;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.task.CustomColumnsManager;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.test.task.TaskTestCase;

public class ExportRessourceFileWizardImplTest  extends TaskTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    TaskDefaultColumn.setLocaleApi(null);
  }

  public void testExportRessources() throws Exception {
    HumanResourceManager hrManager = new HumanResourceManager(null, new CustomColumnsManager());
    IGanttProject ganttProject = new GanttProjectImpl();

    // on fait la création des tâches
    ganttProject.getTaskManager().createTask();
    ganttProject.getTaskManager().createTask();

    HumanResource alice = hrManager.create("Alice", 1);
    HumanResource bob = hrManager.create("Bob", 2);

  // on fait la création des ressources
    ganttProject.getHumanResourceManager().getResources().add(alice);
    ganttProject.getHumanResourceManager().getResources().add(bob);

    ExportRessourceFileWizardImpl exportRessourceFileWizard = new ExportRessourceFileWizardImpl(null ,ganttProject,"Un test ");

    // on s'assure qu'au début on a deux Task
    assertEquals(ganttProject.getTaskManager().getTasks().length,2);

    // on test la méthode qu'on a crée
    exportRessourceFileWizard.deleteTasksAndKeepRessources(ganttProject);

    // On vérifie que seulement les ressources sont encore là
    assertEquals(ganttProject.getTaskManager().getTasks().length,0);
    assertEquals(ganttProject.getHumanResourceManager().getResources().size(),2);
  }

}
