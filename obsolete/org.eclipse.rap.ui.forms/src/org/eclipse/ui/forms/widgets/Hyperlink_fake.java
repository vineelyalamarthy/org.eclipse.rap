// Created on Jul 6, 2007
package org.eclipse.ui.forms.widgets;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.IHyperlinkListener;


public class Hyperlink_fake extends CLabel {

  public Hyperlink_fake( Composite parent, int style ) {
    super( parent, style );
  }

  public void addHyperlinkListener( IHyperlinkListener listener ) {
  }

  public void removeHyperlinkListener( IHyperlinkListener listener ) {
  }

  public void setUnderlined( boolean b ) {
  }
}
