package net.sourceforge.ganttproject.export;

import biz.ganttproject.core.option.BooleanOption;
import biz.ganttproject.core.option.DefaultBooleanOption;
import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.projectwizard.WizardImpl;
import net.sourceforge.ganttproject.task.Task;

import javax.swing.*;
import java.io.File;
import java.net.URL;

public class ExportRessourceFileWizardImpl extends WizardImpl {

  public State myState;
  private final IGanttProject myProject;

  private  Exporter ourLastSelectedExporter;

  public ExportRessourceFileWizardImpl(UIFacade uiFacade, IGanttProject project, String title) {
    super(uiFacade, title);
    myProject = project;
    myState = new State();
  }

  @Override
  protected void onOkPressed() {
    super.onOkPressed();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          ExportFinalizationJob finalizationJob = new ExportFinalizationJobImpl();
          if ("file".equals(myState.getUrl().getProtocol())) {
            //on veut seulement les ressources, donc on enlève les tâches et on garde les ressources
            deleteTasksAndKeepRessources(myProject);
            myState.getExporter().run(new File(myState.getUrl().toURI()), finalizationJob);
          }
        } catch (Exception e) {
          GPLogger.log(e);
        }
      }
    });
  }

  public IGanttProject deleteTasksAndKeepRessources(IGanttProject myProject)
  {
    for(Task task :myProject.getTaskManager().getTasks() )
    {
      myProject.getTaskManager().deleteTask(task);
    }
    return myProject;
  }

  class State {
    private Exporter myExporter;
    private URL myUrl;

    Exporter getExporter() {
      return myExporter;
    }
    public URL getUrl() {
      return myUrl;
    }
  }

  public class ExportFinalizationJobImpl implements ExportFinalizationJob {
      @Override
      public void run(File[] exportedFiles) {

      }
    }
  }

