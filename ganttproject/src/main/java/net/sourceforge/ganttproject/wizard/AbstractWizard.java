/*
Copyright (C) 2013 BarD Software s.r.o

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
package net.sourceforge.ganttproject.wizard;

import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.UIFacade.Centering;

import javax.swing.*;
import java.util.List;

/**
 * A wizard abstraction capable of managing wizard pages and showing them in the UI
 * according to the user actions.
 *
 * @author dbarashev (Dmitry Barashev)
 */
public class AbstractWizard extends Wizard {

  private Runnable myOkRunnable;

  public AbstractWizard(UIFacade uiFacade, String title, WizardPage firstPage) {
    super(uiFacade, title);
    myPagesContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    addPageComponent(firstPage);
    myPages.add(firstPage);
    myDialog.layout();
    myDialog.center(Centering.WINDOW);
    adjustButtonState();
  }

  @Override
  protected void onOkPressed() {
    myOkRunnable.run();
  }

  @Override
  protected void onCancelPressed() {}

  @Override
  protected boolean canFinish() {
    return myOkRunnable != null;
  }

  private boolean isExistingNextPage(WizardPage page) {
    if (page == null) {
      return false;
    }
    int idxPage = myPages.indexOf(page);
    return (idxPage != -1 && myCurrentPage == idxPage - 1);
  }

  /**
   * Active wizard page can call this method to set a next page.
   *
   * @param page next page
   */
  @Override
  public void setNextPage(WizardPage page) {
    boolean isExisting = isExistingNextPage(page);
    if (!isExisting) {
      List<WizardPage> tail = myPages.subList(myCurrentPage + 1, myPages.size());
      for (WizardPage tailPage : tail) {
        JComponent component = myTitle2component.remove(tailPage.getTitle());
        if (component != null) {
          myPagesContainer.remove(component);
        }
      }
      tail.clear();
      if (page != null) {
        myPages.add(page);
      }
    }
    adjustButtonState();
  }

  /**
   * Wizard pages or specific wizard implementations can call this method to set an
   * action to be called when user clicks OK. This makes OK button enabled.
   *
   * @param action action to be called on OK
   */
  @Override
  public void setOkAction(Runnable action) {
    myOkRunnable = action;
    adjustButtonState();
  }

}
