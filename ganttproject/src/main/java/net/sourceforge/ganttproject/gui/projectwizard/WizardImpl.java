/*
GanttProject is an opensource project management tool.
Copyright (C) 2011 GanttProject team

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
package net.sourceforge.ganttproject.gui.projectwizard;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.UIFacade.Centering;
import net.sourceforge.ganttproject.gui.UIFacade.Dialog;
import net.sourceforge.ganttproject.gui.options.TopPanel;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.wizard.Wizard;
import net.sourceforge.ganttproject.wizard.WizardPage;

public abstract class WizardImpl extends Wizard {
  protected final static GanttLanguage language = GanttLanguage.getInstance();

  public WizardImpl(UIFacade uiFacade, String title) {
    super(uiFacade, title);
  }

  public void nextPage() {
    if (myCurrentPage < myPages.size() - 1) {
      getCurrentPage().setActive(null);
      myCurrentPage++;
      getCurrentPage().setActive(this);
      myCardLayout.next(myPagesContainer);
    }
    myDialog.center(Centering.WINDOW);
    adjustButtonState();
  }

  public void backPage() {
    if (myCurrentPage > 0) {
      getCurrentPage().setActive(null);
      myCurrentPage--;
      getCurrentPage().setActive(this);
      myCardLayout.previous(myPagesContainer);
    }
    myDialog.center(Centering.WINDOW);
    adjustButtonState();
  }

  public void show() {
    for (int i = 0; i < myPages.size(); i++) {
      WizardPage page = myPages.get(i);
      addPageComponent(page, i);
    }
    myCardLayout.first(myPagesContainer);
    getCurrentPage().setActive(null);
    myPagesContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    adjustButtonState();
    myDialog.center(Centering.SCREEN);
    myDialog.show();
  }

  protected void addPageComponent(WizardPage page, int index) {
    String currentTitle = getCurrentPage().getTitle();
    JPanel pagePanel = new JPanel(new BorderLayout());
    JComponent titlePanel = TopPanel.create(page.getTitle() + "   (" + language.getText("step") + " " + (index + 1)
        + " " + language.getText("of") + " " + (myPages.size()) + ")", null);
    pagePanel.add(titlePanel, BorderLayout.NORTH);
    JComponent component = (JComponent) page.getComponent();
    component.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    pagePanel.add(component, BorderLayout.CENTER);

    myPagesContainer.add(pagePanel, page.getTitle());
    myCardLayout.show(myPagesContainer, currentTitle);
    myTitle2component.put(page.getTitle(), pagePanel);
  }

  protected void removePageComponent(WizardPage page) {
    JComponent component = myTitle2component.get(page.getTitle());
    if (component != null) {
      myPagesContainer.remove(component);
    }
  }

  public void adjustButtonState() {
    myBackAction.setEnabled(myCurrentPage > 0);
    myNextAction.setEnabled(myCurrentPage < myPages.size() - 1);
    myOkAction.setEnabled(canFinish());
  }

  protected boolean canFinish() {
    return true;
  }

  protected void addPage(WizardPage page) {
    myPages.add(page);
  }

  protected void removePage(WizardPage page) {
    myPages.remove(page);
  }

  @Override
  protected void onOkPressed() {
    getCurrentPage().setActive(null);
  }

  @Override
  protected void onCancelPressed() {
    getCurrentPage().setActive(null);
  }

  private WizardPage getCurrentPage() {
    return myPages.get(myCurrentPage);
  }

  public UIFacade getUIFacade() {
    return myUIFacade;
  }

  public Dialog getDialog() {
    return myDialog;
  }
}
