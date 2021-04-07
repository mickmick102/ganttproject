package net.sourceforge.ganttproject.action.resource;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.export.ExportRessourceFileWizardImpl;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.projectwizard.WizardImpl;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import org.osgi.service.prefs.Preferences;

import java.awt.event.ActionEvent;

public class ResourceExportAction extends ResourceAction {
  private final IGanttProject myProject;

  private final UIFacade myUIFacade;

  private Preferences myPluginPrerences;

  public ResourceExportAction(UIFacade uiFacade, IGanttProject project, Preferences pluginPrerences, HumanResourceManager humanResourceManager) {
    super("resource.export", humanResourceManager);
    myProject = project;
    myUIFacade = uiFacade;
    myPluginPrerences = pluginPrerences;
  }

  @Override
  protected String getIconFilePrefix() {
    return "export_";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (calledFromAppleScreenMenu(e)) {
      return;
    }
    WizardImpl wizard = new ExportRessourceFileWizardImpl(myUIFacade, myProject, "Export Resource Wizard");
    wizard.show();
  }
}
