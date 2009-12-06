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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.rap.themeeditor.SupportedKeywords;
import org.eclipse.rap.themeeditor.ThemeEditorPlugin;
import org.eclipse.rap.themeeditor.editor.source.region.IRegionExt;
import org.eclipse.rwt.internal.theme.ThemeDefElementWrapper;
import org.eclipse.rwt.internal.theme.ThemeDefProperty;
import org.eclipse.rwt.internal.theme.ThemeDefinitionProvider;

public class CSSCompletionProcessor implements IContentAssistProcessor {

  private CSSTokenScanner tokenScanner;

  public CSSCompletionProcessor( final CSSTokenScanner tokenScanner ) {
    this.tokenScanner = tokenScanner;
  }

  public ICompletionProposal[] computeCompletionProposals( final ITextViewer viewer,
                                                           final int offset )
  {
    IRegionExt regionExt = tokenScanner.getRegionExt( offset );
    String currentContent = regionExt.getContent();
    if( offset >= regionExt.getOffset()
        && offset <= ( regionExt.getOffset() + regionExt.getLength() )
        && ( offset - regionExt.getOffset() ) <= currentContent.length() )
    {
      currentContent = currentContent.substring( 0, offset
                                                    - regionExt.getOffset() );
    } else {
      return null;
    }
    String[] proposalStrings = SupportedKeywords.getKeywordsStartWith( currentContent.trim(),
                                                                       regionExt.getKeywordType() );
    if( proposalStrings == null ) {
      return null;
    }
    proposalStrings = filterProposalStrings( proposalStrings, regionExt );
    if( proposalStrings.length == 0 ) {
      return null;
    }
    // just a hack to perform trimStart only, as whitespace at end must remain
    currentContent = currentContent + '#';
    int beginIndex = currentContent.trim().length() - 1;
    ICompletionProposal[] result = new ICompletionProposal[ proposalStrings.length ];
    for( int i = 0; i < proposalStrings.length; i++ ) {
      String additionalInfo = ThemeDefinitionProvider.getDescription( regionExt,
                                                                      proposalStrings[ i ] );
      IContextInformation info = new ContextInformation( proposalStrings[ i ],
                                                         proposalStrings[ i ] );
      result[ i ] = new CompletionProposal( proposalStrings[ i ],
                                            offset - beginIndex,
                                            beginIndex,
                                            proposalStrings[ i ].length(),
                                            ThemeEditorPlugin.getDefault()
                                              .getImage( ThemeEditorPlugin.IMG_FIELD_PRIVATE ),
                                            proposalStrings[ i ],
                                            info,
                                            additionalInfo );
    }
    return result;
  }

  public IContextInformation[] computeContextInformation( final ITextViewer viewer,
                                                          final int offset )
  {
    return null;
  }

  public char[] getCompletionProposalAutoActivationCharacters() {
    return new char[]{
      '.', ':', '['
    };
  }

  public char[] getContextInformationAutoActivationCharacters() {
    return null;
  }

  public IContextInformationValidator getContextInformationValidator() {
    return null;
  }

  public String getErrorMessage() {
    return null;
  }

  private String[] filterProposalStrings( final String[] proposalStrings,
                                          final IRegionExt regionExt )
  {
    String[] result;
    switch( regionExt.getKeywordType() ) {
      case SupportedKeywords.STYLE_TYPE:
        result = filterProposalStyles( proposalStrings, regionExt );
      break;
      case SupportedKeywords.STATE_TYPE:
        result = filterProposalStates( proposalStrings, regionExt );
      break;
      case SupportedKeywords.PROPERTY_TYPE:
        result = filterProposalProperties( proposalStrings, regionExt );
      break;
      default:
        result = proposalStrings;
      break;
    }
    return result;
  }

  private String[] filterProposalStyles( final String[] proposalStrings,
                                         final IRegionExt regionExt )
  {
    String[] result = new String[ 0 ];
    if( regionExt.getTokenType() == CSSTokenProvider.STYLE_TOKEN ) {
      ThemeDefElementWrapper wrapper = ThemeDefinitionProvider.getElementWrapper( regionExt );
      if( wrapper != null ) {
        List resultList = new ArrayList();
        for( int i = 0; i < proposalStrings.length; i++ ) {
          String text = proposalStrings[ i ];
          if( wrapper.element.styleMap.containsKey( text ) ) {
            resultList.add( text );
          }
        }
        result = new String[ resultList.size() ];
        result = ( String[] )resultList.toArray( result );
      }
    }
    return result;
  }

  private String[] filterProposalStates( final String[] proposalStrings,
                                         final IRegionExt regionExt )
  {
    String[] result = new String[ 0 ];
    if( regionExt.getTokenType() == CSSTokenProvider.STATE_TOKEN ) {
      ThemeDefElementWrapper wrapper = ThemeDefinitionProvider.getElementWrapper( regionExt );
      if( wrapper != null ) {
        List resultList = new ArrayList();
        for( int i = 0; i < proposalStrings.length; i++ ) {
          String text = proposalStrings[ i ];
          if( wrapper.element.stateMap.containsKey( text ) ) {
            resultList.add( text );
          }
        }
        result = new String[ resultList.size() ];
        result = ( String[] )resultList.toArray( result );
      }
    }
    return result;
  }

  private String[] filterProposalProperties( final String[] proposalStrings,
                                             final IRegionExt regionExt )
  {
    String[] result = new String[ 0 ];
    if( regionExt.getTokenType() == CSSTokenProvider.PROPERTY_TOKEN ) {
      OutlineRegion[] regions = tokenScanner.getOutlineRegionsArray();
      OutlineRegion outlineRegion = null;
      for( int i = 0; i < regions.length; i++ ) {
        OutlineRegion next = regions[ i ];
        if( next.getOffset() <= regionExt.getOffset() ) {
          outlineRegion = next;
        }
      }
      if( outlineRegion != null ) {
        List resultList = new ArrayList();
        for( int i = 0; i < outlineRegion.getElements().length; i++ ) {
          ThemeDefElementWrapper wrapper = ThemeDefinitionProvider.getElementWrapper( outlineRegion.getElements()[ i ] );
          if( wrapper != null ) {
            for( int j = 0; j < proposalStrings.length; j++ ) {
              String text = proposalStrings[ j ];
              for( int k = 0; k < wrapper.element.properties.length; k++ ) {
                ThemeDefProperty property = wrapper.element.properties[ k ];
                if( property.name.equals( text ) ) {
                  if( !resultList.contains( text ) ) {
                    resultList.add( text );
                  }
                }
              }
            }
          }
        }
        result = new String[ resultList.size() ];
        result = ( String[] )resultList.toArray( result );
        Arrays.sort( result );
      }
    }
    return result;
  }
}
