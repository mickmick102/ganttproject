package net.sourceforge.ganttproject.wizard;

import com.google.common.collect.Maps;
import net.sourceforge.ganttproject.action.CancelAction;
import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.action.OkAction;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.options.TopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

public abstract class Wizard {
  protected final ArrayList<WizardPage> myPages = new ArrayList<WizardPage>();
  protected final Map<String, JComponent> myTitle2component = Maps.newHashMap();
  protected final JPanel myPagesContainer;
  protected final CardLayout myCardLayout;
  protected final AbstractAction myNextAction;
  protected final AbstractAction myBackAction;
  protected final AbstractAction myOkAction;
  protected final AbstractAction myCancelAction;
  protected final UIFacade myUIFacade;
  protected final String myTitle;
  protected final UIFacade.Dialog myDialog;
  protected int myCurrentPage;

  public Wizard(UIFacade uiFacade, String title) {
    myCardLayout = new CardLayout();
    myPagesContainer = new JPanel(myCardLayout);
    myNextAction = new GPAction("next") {
      @Override
      public void actionPerformed(ActionEvent e) {
        nextPage();
      }
    };
    myBackAction = new GPAction("back") {
      @Override
      public void actionPerformed(ActionEvent e) {
        backPage();
      }
    };
    myCancelAction = new CancelAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onCancelPressed();
      }
    };
    myUIFacade = uiFacade;
    myTitle = title;
    myOkAction = new OkAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onOkPressed();
      }
    };
    myDialog = myUIFacade.createDialog(myPagesContainer, new Action[] { myBackAction, myNextAction, myOkAction,
      myCancelAction }, myTitle);
  }

  protected void nextPage() {
    assert myCurrentPage + 1 < myPages.size() : "It is a bug: we have no next page while Next button is enabled and has been pressed";
    getCurrentPage().setActive(null);
    WizardPage nextPage = myPages.get(myCurrentPage + 1);
    if (myTitle2component.get(nextPage.getTitle()) == null) {
      addPageComponent(nextPage);
    }
    myCurrentPage++;
    nextPage.setActive(this);
    myCardLayout.show(myPagesContainer, nextPage.getTitle());
    myDialog.center(UIFacade.Centering.WINDOW);
    myDialog.layout();
    adjustButtonState();
  }

  protected void addPageComponent(WizardPage page) {
    if (myTitle2component.get(page.getTitle()) == null) {
      JComponent c = wrapePageComponent(page.getTitle(), page.getComponent());
      myPagesContainer.add(c, page.getTitle());
      myTitle2component.put(page.getTitle(), c);
    }
  }

  private JComponent wrapePageComponent(String title, JComponent c) {
    JPanel pagePanel = new JPanel(new BorderLayout());
    JComponent titlePanel = TopPanel.create(title, null);
    pagePanel.add(titlePanel, BorderLayout.NORTH);
    c.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    pagePanel.add(c, BorderLayout.CENTER);
    return pagePanel;
  }

  protected void backPage() {
    if (myCurrentPage > 0) {
      getCurrentPage().setActive(null);
      myCurrentPage--;
      myCardLayout.show(myPagesContainer, getCurrentPage().getTitle());
      getCurrentPage().setActive(this);
    }
    //myDialog.center(Centering.WINDOW);
    adjustButtonState();
  }

  public void show() {
    myCardLayout.first(myPagesContainer);
    getCurrentPage().setActive(this);
    adjustButtonState();
    myDialog.center(UIFacade.Centering.SCREEN);
    myDialog.show();
  }

  protected void adjustButtonState() {
    myBackAction.setEnabled(myCurrentPage > 0);
    myNextAction.setEnabled(myCurrentPage < myPages.size() - 1);
    myOkAction.setEnabled(canFinish());
  }

  protected abstract void onOkPressed();

  protected abstract void onCancelPressed();

  protected abstract boolean canFinish();

  public abstract void setNextPage(WizardPage page);

  public abstract void setOkAction(Runnable action);

  private WizardPage getCurrentPage() {
    return myPages.get(myCurrentPage);
  }

  public UIFacade getUIFacade() {
    return myUIFacade;
  }

  public UIFacade.Dialog getDialog() {
    return myDialog;
  }
}
