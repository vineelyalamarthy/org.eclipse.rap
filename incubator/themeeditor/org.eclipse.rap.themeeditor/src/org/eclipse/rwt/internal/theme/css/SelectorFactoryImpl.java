/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.rwt.internal.theme.css;

import org.w3c.css.sac.*;

/**
 * SelectorFacory implementation for parsing RAP theme files. All returned
 * selectors implement the interface {@link SelectorExt}.
 */
public class SelectorFactoryImpl implements SelectorFactory {

  private final CssFileReader reader;

  public SelectorFactoryImpl( final CssFileReader reader ) {
    this.reader = reader;
  }

  public ElementSelector createElementSelector( final String namespaceURI,
                                                final String tagName )
    throws CSSException
  {
    return new ElementSelectorImpl( tagName );
  }

  public ConditionalSelector createConditionalSelector( final SimpleSelector selector,
                                                        final Condition condition )
    throws CSSException
  {
    return new ConditionalSelectorImpl( selector, condition );
  }

  // ==========================================================================
  // Not supported by RAP, but used by Theme Editor
  
  public DescendantSelector createChildSelector( final Selector parent,
                                                 final SimpleSelector child )
  throws CSSException
  {
//    return new ChildSelectorImpl( parent, child );
    String mesg = "Child selectors not supported by RAP - ignored";
    reader.addProblem( new CSSException( mesg ) );
    /* BEGIN Modification for Theme Editor */
    return new EmptyChildSelectorImpl( parent, child );
    /* END Modification for Theme Editor */
  }

  public ElementSelector createPseudoElementSelector( final String namespaceURI,
                                                      final String pseudoName )
    throws CSSException
  {
    String mesg = "Pseudo element selectors not supported by RAP - ignored";
    reader.addProblem( new CSSException( mesg ) );
    /* BEGIN Modification for Theme Editor */
    return new EmptyElementSelectorImpl( pseudoName );
    /* END Modification for Theme Editor */
  }

  public DescendantSelector createDescendantSelector( final Selector parent,
                                                      final SimpleSelector descendant )
    throws CSSException
  {
    String mesg = "Descendant selectors not supported by RAP - ignored";
    reader.addProblem( new CSSException( mesg ) );
    /* BEGIN Modification for Theme Editor */
    return new EmptyDescendantSelectorImpl( parent, descendant );
    /* END Modification for Theme Editor */
  }

  public SiblingSelector createDirectAdjacentSelector( final short nodeType,
                                                       final Selector child,
                                                       final SimpleSelector directAdjacent )
    throws CSSException
  {
    String mesg = "Sibling selectors not supported by RAP - ignored";
    reader.addProblem( new CSSException( mesg ) );
    /* BEGIN Modification for Theme Editor */
    return new EmptySiblingSelectorImpl( nodeType, child, directAdjacent );
    /* END Modification for Theme Editor */
  }

  // ==========================================================================
  // Not implemented in CSS 2

  public SimpleSelector createRootNodeSelector() throws CSSException {
    throw new CSSException( "Root node selectors not supported by CSS2" );
  }

  public CharacterDataSelector createTextNodeSelector( final String data )
    throws CSSException
  {
    throw new CSSException( "Text node selectors not supported by CSS2" );
  }

  public CharacterDataSelector createCDataSectionSelector( final String data )
    throws CSSException
  {
    throw new CSSException( "CData section selectors not supported by CSS2" );
  }

  public ProcessingInstructionSelector createProcessingInstructionSelector( final String target,
                                                                            final String data )
    throws CSSException
  {
    throw new CSSException( "Processing instruction selectors not supported by CSS2" );
  }

  public CharacterDataSelector createCommentSelector( final String data )
    throws CSSException
  {
    throw new CSSException( "Comment selectors not supported by CSS2" );
  }

  public SimpleSelector createAnyNodeSelector() throws CSSException {
    throw new CSSException( "Any-node selectors not supported by CSS2" );
  }

  public NegativeSelector createNegativeSelector( final SimpleSelector selector )
    throws CSSException
  {
    throw new CSSException( "Negative selectors not supported by CSS2" );
  }
}
