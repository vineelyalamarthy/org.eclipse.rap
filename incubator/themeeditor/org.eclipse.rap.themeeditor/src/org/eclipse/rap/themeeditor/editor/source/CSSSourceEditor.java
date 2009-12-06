/*******************************************************************************
 * Copyright (c) 2008 Mathias Schaeffner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mathias Schaeffner - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.themeeditor.editor.source;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.rap.themeeditor.editor.CSSContentOutlinePage;
import org.eclipse.rap.themeeditor.editor.IOutlineSelectionChangedListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * The general editor in the Source Tab. Providing content assists, syntax
 * coloring and Outline contribution.
 */
public class CSSSourceEditor extends TextEditor
  implements IOutlineSelectionChangedListener
{

  private CSSTokenScanner tokenScanner;
  private CSSContentOutlinePage outlinePage;

  protected void initializeEditor() {
    super.initializeEditor();
    tokenScanner = new CSSTokenScanner( this );
    CSSSourceViewerConfiguration configuration
      = new CSSSourceViewerConfiguration( tokenScanner );
    setSourceViewerConfiguration( configuration );
  }

  public void createPartControl( Composite parent ) {
    super.createPartControl( parent );
    final IDocument doc = getDocument();
    // add listener and call setRange manually, because it isnt't called
    // automatically from jface if backspace key is performed
    doc.addDocumentListener( new IDocumentListener() {

      public void documentAboutToBeChanged( DocumentEvent event ) {
        ;
      }

      public void documentChanged( DocumentEvent event ) {
        tokenScanner.setRange( doc, 0, doc.getLength() );
        outlinePage.setInput( getSelectorTokens() );
      }
    } );
  }

  protected ISourceViewer createSourceViewer( final Composite parent,
                                              final IVerticalRuler ruler,
                                              final int styles )
  {
    return super.createSourceViewer( parent, ruler, styles );
  }

  public Object[] getSelectorTokens() {
    return tokenScanner.getOutlineRegionsArray();
  }

  protected void handleCursorPositionChanged() {
    super.handleCursorPositionChanged();
    ISelection selection = getSelectionProvider().getSelection();
    if( selection instanceof ITextSelection ) {
      int offset = ( ( ITextSelection )selection ).getOffset();
      int index = getRuleNumber( offset );
      if( outlinePage != null ) {
        outlinePage.setSelection( index );
      }
    }
  }

  public void doCursorPositionChanged() {
    handleCursorPositionChanged();
  }

  protected void createActions() {
    super.createActions();
    IAction action = new ContentAssistAction( getResourceBundle(),
                                              "ContentAssistProposal.",
                                              this );
    action.setActionDefinitionId( ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS );
    final String actionId = "RAP_THEME_EDITOR_CONTENT_ASSIST_PROPOSALS";
    setAction( actionId, action );
    markAsStateDependentAction( actionId, true );
  }

  /**
   * Notification from the Outline when the user changed its selection.
   */
  public void outlineSelectionChanged( int newIndex, Object item ) {
    if( item == null ) {
      resetHighlightRange();
    } else {
      if( item instanceof OutlineRegion ) {
        OutlineRegion region = ( OutlineRegion )item;
        int start = region.getOffset();
        int length = region.getLength();
        try {
          String regionText = getDocument().get( start, length );
          int i = 0;
          while( i < regionText.length()
                 && Character.isWhitespace( regionText.charAt( i ) ) )
          {
            i++;
          }
          setHighlightRange( start + i, length - i, true );
        } catch( IllegalArgumentException exception ) {
          resetHighlightRange();
        } catch( BadLocationException exception ) {
          resetHighlightRange();
        }
      } else {
        resetHighlightRange();
      }
    }
  }

  public IDocument getDocument() {
    return getDocumentProvider().getDocument( getEditorInput() );
  }

  public Object getAdapter( Class adapter ) {
    Object result;
    if( IContentOutlinePage.class.equals( adapter ) ) {
      if( outlinePage == null ) {
        outlinePage = new CSSContentOutlinePage( this );
        outlinePage.setInput( getSelectorTokens() );
      }
      result = outlinePage;
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }

  /**
   * Returns the index of a rule in the StyleSheet at a given offset position
   * within the document.
   */
  private int getRuleNumber( final int offset ) {
    int result = -1;
    Iterator it = tokenScanner.getOutlineRegionsList().iterator();
    while( it.hasNext() ) {
      IRegion regionExt = ( IRegion )it.next();
      if( regionExt.getOffset() <= offset ) {
        result++;
      }
    }
    if( result < 0 ) {
      result = 0;
    }
    return result;
  }

  private ResourceBundle getResourceBundle() {
    return new ResourceBundle() {
      
      protected Object handleGetObject( String key ) {
        return null;
      }
      
      public Enumeration getKeys() {
        return null;
      }
    };
  }
}
