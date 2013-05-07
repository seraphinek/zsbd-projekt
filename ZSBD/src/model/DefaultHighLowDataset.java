package model;

/* =========================================================== 
 * JFreeChart : a free chart library for the Java(tm) platform 
 * =========================================================== 
 * 
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors. 
 * 
 * Project Info: http://www.jfree.org/jfreechart/index.html 
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version. 
 * 
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, 
 * USA. 
 * 
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.] 
 * 
 * -------------------------- 
 * DefaultHighLowDataset.java 
 * -------------------------- 
 * (C) Copyright 2002-2005, by Object Refinery Limited. 
 * 
 * Original Author: David Gilbert (for Object Refinery Limited); 
 * Contributor(s): -; 
 * 
 * $Id: DefaultHighLowDataset.java,v 1.6.2.1 2005/10/25 21:36:51 mungady Exp $ 
 * 
 * Changes 
 * ------- 
 * 21-Mar-2002 : Version 1 (DG); 
 * 07-Oct-2002 : Fixed errors reported by Checkstyle (DG); 
 * 06-May-2004 : Now extends AbstractXYDataset and added new methods from 
 * HighLowDataset (DG); 
 * 15-Jul-2004 : Switched getX() with getXValue() and getY() with 
 * getYValue() (DG); 
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

/**
 * A simple implementation of the {@link OHLCDataset}.
 */
public class DefaultHighLowDataset extends AbstractXYDataset implements
		OHLCDataset {

	private static final long serialVersionUID = -2469592360859869092L;

	/** The series key. */
	private final Comparable<String> seriesKey;

	/** Storage for the dates. */
	private final List<Date> date = new ArrayList<Date>();

	/** Storage for the high values. */
	private final List<Number> high = new ArrayList<Number>();

	/** Storage for the low values. */
	private final List<Number> low = new ArrayList<Number>();

	/** Storage for the open values. */
	private final List<Number> open = new ArrayList<Number>();

	/** Storage for the close values. */
	private final List<Number> close = new ArrayList<Number>();

	/** Storage for the volume values. */
	private final List<Number> volume = new ArrayList<Number>();

	/**
	 * Constructs a new high/low/open/close dataset.
	 * <p>
	 * The current implementation allows only one series in the dataset. This
	 * may be extended in a future version.
	 * 
	 * @param seriesKey
	 *            the key for the series.
	 * @param date
	 *            the dates.
	 * @param high
	 *            the high values.
	 * @param low
	 *            the low values.
	 * @param open
	 *            the open values.
	 * @param close
	 *            the close values.
	 * @param volume
	 *            the volume values.
	 */
	public DefaultHighLowDataset(Comparable<String> seriesKey, Date[] idate,
			double[] ihigh, final double[] ilow, double[] iopen,
			final double[] iclose, double[] ivolume) {

		this.seriesKey = seriesKey;
		for (int i = 0; i < idate.length; ++i) {
			this.date.add(idate[i]);
			this.high.add(ihigh[i]);
			this.low.add(ilow[i]);
			this.open.add(iopen[i]);
			this.close.add(iclose[i]);
			this.volume.add(ivolume[i]);

		}

	}

	public DefaultHighLowDataset(Comparable<String> seriesKey) {
		this.seriesKey = seriesKey;
	}

	public void add(Date idate, double ihigh, double ilow, double iopen,
			double iclose, double ivolume) {
		this.date.add(idate);
		this.high.add(ihigh);
		this.low.add(ilow);
		this.open.add(iopen);
		this.close.add(iclose);
		this.volume.add(ivolume);
		fireDatasetChanged();
	}

	public DefaultHighLowDataset compressData(int mult) {
		DefaultHighLowDataset compress = new DefaultHighLowDataset(
				this.seriesKey);
		int iseries = getSeriesCount();

		if (this.date.size() >= 2) {

			long milliSeconds = (this.date.get(1).getTime() - this.date.get(0)
					.getTime()) * mult;

			int index = 0;
			while (index < this.date.size()) {
				if ((this.date.get(index).getTime() % milliSeconds) == 0) {
					Date date = getXDate(iseries, index);
					double dOpen = getOpenValue(iseries, index);
					double dHigh = getHighValue(iseries, index);
					double dLow = getLowValue(iseries, index);
					double dVolume = getVolumeValue(iseries, index);

					for (int i = index + 1; i < index + mult; ++i) {
						double dNewHigh = getHighValue(iseries, i);
						double dNewLow = getLowValue(iseries, i);
						dVolume += getVolumeValue(iseries, i);

						if (dNewHigh > dHigh)
							dHigh = dNewHigh;
						if (dNewLow < dLow)
							dLow = dNewLow;
					}
					index += (mult - 1);
					double dClose = getCloseValue(iseries, index);
					compress.add(date, dHigh, dLow, dOpen, dClose, dVolume);
					index++;
				} else {
					index++;
				}
			}

		}

		return compress;
	}

	public void addOrUpdate(Date idate, double ihigh, double ilow,
			double iopen, double iclose, double ivolume) {

		int index = Collections.binarySearch(this.date, idate);
		if (index >= 0) {
			this.date.set(index, idate);
			this.high.set(index, ihigh);
			this.low.set(index, ilow);
			this.open.set(index, iopen);
			this.close.set(index, iclose);
			this.volume.set(index, ivolume);
		} else {
			this.add(idate, ihigh, ilow, iopen, iclose, ivolume);

		}
	}

	public void removeLast() {
		int dataSize = this.date.size() - 1;
		if (dataSize >= 0) {
			this.date.remove(dataSize);
			this.high.remove(dataSize);
			this.low.remove(dataSize);
			this.open.remove(dataSize);
			this.close.remove(dataSize);
			this.volume.remove(dataSize);
		}
	}

	public void removeFirst() {
		int dataSize = this.date.size() - 1;
		if (dataSize >= 0) {
			this.date.remove(0);
			this.high.remove(0);
			this.low.remove(0);
			this.open.remove(0);
			this.close.remove(0);
			this.volume.remove(0);
		}
	}

	/**
	 * Returns the for the series stored in this dataset.
	 * 
	 * @param i
	 *            the index of the series. Currently ignored.
	 * 
	 * @return The key for this series.
	 */
	@Override
	public Comparable<String> getSeriesKey(int i) {
		return this.seriesKey;
	}

	/**
	 * Returns the x-value for one item in a series. The value returned is a
	 * <code>Long</code> instance generated from the underlying
	 * <code>Date</code> object.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The x-value.
	 */
	@Override
	public Number getX(int series, int item) {
		Date tdate = this.date.get(item);
		return new Long(tdate.getTime());
	}

	/**
	 * Returns the x-value for one item in a series, as a Date.
	 * <p>
	 * This method is provided for convenience only.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The x-value as a Date.
	 */
	public Date getXDate(int series, int item) {
		return this.date.get(item);
	}

	/**
	 * Returns the y-value for one item in a series.
	 * <p>
	 * This method (from the {@link XYDataset} interface) is mapped to the
	 * {@link #getCloseValue(int, int)} method.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The y-value.
	 */
	@Override
	public Number getY(int series, int item) {
		return getClose(series, item);
	}

	/**
	 * Returns the high-value for one item in a series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The high-value.
	 */
	@Override
	public Number getHigh(int series, int item) {
		return this.high.get(item);
	}

	/**
	 * Returns the high-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The high-value.
	 */
	@Override
	public double getHighValue(int series, int item) {
		double result = Double.NaN;
		Number high = getHigh(series, item);
		if (high != null) {
			result = high.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the low-value for one item in a series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The low-value.
	 */
	@Override
	public Number getLow(int series, int item) {
		return this.low.get(item);
	}

	/**
	 * Returns the low-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The low-value.
	 */
	@Override
	public double getLowValue(int series, int item) {
		double result = Double.NaN;
		Number low = getLow(series, item);
		if (low != null) {
			result = low.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the open-value for one item in a series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The open-value.
	 */
	@Override
	public Number getOpen(int series, int item) {
		return this.open.get(item);
	}

	/**
	 * Returns the open-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The open-value.
	 */
	@Override
	public double getOpenValue(int series, int item) {
		double result = Double.NaN;
		Number open = getOpen(series, item);
		if (open != null) {
			result = open.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the close-value for one item in a series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The close-value.
	 */
	@Override
	public Number getClose(int series, int item) {
		return this.close.get(item);
	}

	/**
	 * Returns the close-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The close-value.
	 */
	@Override
	public double getCloseValue(int series, int item) {
		double result = Double.NaN;
		Number close = getClose(series, item);
		if (close != null) {
			result = close.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the volume-value for one item in a series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The volume-value.
	 */
	@Override
	public Number getVolume(int series, int item) {
		return this.volume.get(item);
	}

	/**
	 * Returns the volume-value (as a double primitive) for an item within a
	 * series.
	 * 
	 * @param series
	 *            the series (zero-based index).
	 * @param item
	 *            the item (zero-based index).
	 * 
	 * @return The volume-value.
	 */
	@Override
	public double getVolumeValue(int series, int item) {
		double result = Double.NaN;
		Number volume = getVolume(series, item);
		if (volume != null) {
			result = volume.doubleValue();
		}
		return result;
	}

	/**
	 * Returns the number of series in the dataset.
	 * <p>
	 * This implementation only allows one series.
	 * 
	 * @return The number of series.
	 */
	@Override
	public int getSeriesCount() {
		return 1;
	}

	/**
	 * Returns the number of items in the specified series.
	 * 
	 * @param series
	 *            the index (zero-based) of the series.
	 * 
	 * @return The number of items in the specified series.
	 */
	@Override
	public int getItemCount(int series) {
		return this.date.size();
	}

	/**
	 * Constructs an array of Number objects from an array of doubles.
	 * 
	 * @param data
	 *            the double values to convert.
	 * 
	 * @return The data as an array of Number objects.
	 */
	public static Number[] createNumberArray(double[] data) {
		Number[] result = new Number[data.length];
		for (int i = 0; i < data.length; i++) {
			result[i] = new Double(data[i]);
		}
		return result;
	}

}