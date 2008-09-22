// Created on 14.09.2007
package org.eclipse.rap.rms.ui.internal.wizards;

import org.eclipse.rap.rms.data.IEntity;


public interface INewEntityPage {
  boolean create();
  IEntity getEntity();
}
