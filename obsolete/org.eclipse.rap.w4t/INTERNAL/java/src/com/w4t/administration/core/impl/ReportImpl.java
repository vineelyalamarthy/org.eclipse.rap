/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package com.w4t.administration.core.impl;

import com.w4t.W4TContext;
import com.w4t.WebComponentStatistics;
import com.w4t.administration.core.IReport;

class ReportImpl extends ModelElementImpl implements IReport {

  ReportImpl() {
  }

  public String getStatisticSummary() {
    WebComponentStatistics statistics = W4TContext.getStatistics( false );
    return    statistics.getSessionCountText()
            + "\n"
            + statistics.getApplicationUptimeText()
            + "\n"
            + statistics.getOccupiedMemoryKBText()
            + "\n"
            + statistics.getStatisticsTimeText();
  }
}