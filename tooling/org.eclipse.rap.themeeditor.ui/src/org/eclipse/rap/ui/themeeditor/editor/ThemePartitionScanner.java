package org.eclipse.rap.ui.themeeditor.editor;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class ThemePartitionScanner extends RuleBasedPartitionScanner {

  public final static String CSS_COMMENT = "__css_comment";

  public ThemePartitionScanner() {
    IPredicateRule[] rules = new IPredicateRule[ 1 ];
    rules[ 0 ] = new MultiLineRule( "/*", "*/", getCommentToken() );
    setPredicateRules( rules );
  }

  protected Token getCommentToken() {
    return new Token( CSS_COMMENT );
  }
}
