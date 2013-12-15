package rsbudget.data.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rsbaselib.util.MonthLabelProvider;

/**
 * Helper class.
 * @author ralph
 *
 */
public class SequenceNumber {
	
	public static final SequenceNumber Q1 = new SequenceNumber(1, Calendar.JANUARY, 3);
	public static final SequenceNumber Q2 = new SequenceNumber(2, Calendar.FEBRUARY, 3);
	public static final SequenceNumber Q3 = new SequenceNumber(3, Calendar.MARCH, 3);
	
	public static final SequenceNumber H1 = new SequenceNumber(1, Calendar.JANUARY, 6);
	public static final SequenceNumber H2 = new SequenceNumber(2, Calendar.FEBRUARY, 6);
	public static final SequenceNumber H3 = new SequenceNumber(3, Calendar.MARCH, 6);
	public static final SequenceNumber H4 = new SequenceNumber(4, Calendar.APRIL, 6);
	public static final SequenceNumber H5 = new SequenceNumber(5, Calendar.MAY, 6);
	public static final SequenceNumber H6 = new SequenceNumber(6, Calendar.JUNE, 6);

	public static final SequenceNumber M1 = new SequenceNumber(1, Calendar.JANUARY);
	public static final SequenceNumber M2 = new SequenceNumber(2, Calendar.FEBRUARY);
	public static final SequenceNumber M3 = new SequenceNumber(3, Calendar.MARCH);
	public static final SequenceNumber M4 = new SequenceNumber(4, Calendar.APRIL);
	public static final SequenceNumber M5 = new SequenceNumber(5, Calendar.MAY);
	public static final SequenceNumber M6 = new SequenceNumber(6, Calendar.JUNE);
	public static final SequenceNumber M7 = new SequenceNumber(7, Calendar.JULY);
	public static final SequenceNumber M8 = new SequenceNumber(8, Calendar.AUGUST);
	public static final SequenceNumber M9 = new SequenceNumber(9, Calendar.SEPTEMBER);
	public static final SequenceNumber M10 = new SequenceNumber(10, Calendar.OCTOBER);
	public static final SequenceNumber M11 = new SequenceNumber(11, Calendar.NOVEMBER);
	public static final SequenceNumber M12 = new SequenceNumber(12, Calendar.DECEMBER);
	
	public static final List<SequenceNumber> QUARTERLY_LIST = new ArrayList<>();
	public static final List<SequenceNumber> HALF_YEARLY_LIST = new ArrayList<>();
	public static final List<SequenceNumber> YEARLY_LIST = new ArrayList<>();
	
	static {
		QUARTERLY_LIST.add(Q1);
		QUARTERLY_LIST.add(Q2);
		QUARTERLY_LIST.add(Q3);
		HALF_YEARLY_LIST.add(H1);
		HALF_YEARLY_LIST.add(H2);
		HALF_YEARLY_LIST.add(H3);
		HALF_YEARLY_LIST.add(H4);
		HALF_YEARLY_LIST.add(H5);
		HALF_YEARLY_LIST.add(H6);
		YEARLY_LIST.add(M1);
		YEARLY_LIST.add(M2);
		YEARLY_LIST.add(M3);
		YEARLY_LIST.add(M4);
		YEARLY_LIST.add(M5);
		YEARLY_LIST.add(M6);
		YEARLY_LIST.add(M7);
		YEARLY_LIST.add(M8);
		YEARLY_LIST.add(M9);
		YEARLY_LIST.add(M10);
		YEARLY_LIST.add(M11);
		YEARLY_LIST.add(M12);
	}
	
	
	private String display;
	private int no;
	
	/**
	 * Constructor.
	 */
	public SequenceNumber(int no, int month) {
		this(no, MonthLabelProvider.INSTANCE.getText(month));
	}
	
	/**
	 * Constructor.
	 */
	public SequenceNumber(int no, int firstMonth, int cycle) {
		this(no, getDisplay(firstMonth, cycle));
	}
	
	/**
	 * Constructor.
	 */
	public SequenceNumber(int no, String display) {
		this.no = no;
		this.display = display;
	}
	
	/**
	 * Returns the number
	 * @return
	 */
	public int getNo() {
		return no;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return display;
	}
	
	/**
	 * Returns the sequence from the given list or null.
	 * @param no
	 * @param list
	 * @return
	 */
	public static SequenceNumber getSequence(int no, List<SequenceNumber> list) {
		if (list == null) return null;
		for (SequenceNumber seq : list) {
			if (seq.getNo() == no) return seq;
		}
		return null;
	}
	
	/**
	 * Returns the sequence from the given period or null.
	 * @param no
	 * @param period
	 * @return
	 */
	public static SequenceNumber getSequence(int no, PlannedPeriod period) {
		return getSequence(no, getList(period));
	}
	
	/**
	 * Returns the appropriate list for the period.
	 * @param period
	 * @return
	 */
	public static List<SequenceNumber> getList(PlannedPeriod period) {
		switch (period) {
		case YEARLY: return YEARLY_LIST;
		case HALF_YEARLY: return HALF_YEARLY_LIST;
		case QUARTERLY: return QUARTERLY_LIST;
		default:
		}
		return null;
	}
	
	public static String getDisplay(int firstMonth, int cycle) {
		StringBuffer rc = new StringBuffer();
		for (int month=firstMonth; month < 12; month+=cycle) {
			if (rc.length() > 0) rc.append(" / ");
			rc.append(MonthLabelProvider.INSTANCE.getText(month));
		}
		return rc.toString();
	}
}