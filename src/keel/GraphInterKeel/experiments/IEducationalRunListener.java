/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010
	
	F. Herrera (herrera@decsai.ugr.es)
    L. SÃ¡nchez (luciano@uniovi.es)
    J. AlcalÃ¡-Fdez (jalcala@decsai.ugr.es)
    S. GarcÃ­a (sglopez@ujaen.es)
    A. FernÃ¡ndez (alberto.fernandez@ujaen.es)
    J. Luengo (julianlm@decsai.ugr.es)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see http://www.gnu.org/licenses/
  
**********************************************************************/

/**
 * <p>
 * @author Written by Juan Carlos FernÃ¡ndez and Pedro Antonio GutiÃ©rrez (University of CÃ³rdoba) 23/08/2009
 * @version 1.0
 * @since JDK1.5
 * </p>
 */

package keel.GraphInterKeel.experiments;

public interface IEducationalRunListener<A extends EducationalRun>{

 /*
 * <p>
 * Listener interface for EducationalRunEven
 * Is used when the GUI of partitions is closed by used
 * </p>
 */

	/**
     * <p>
	 * Window of partitions is closed by user, then this method is invoqued
     * </p>
     *
     * @param event  Event
	 */	
	public void closedEducationalExec(EducationalRunEvent<A> event);
}
