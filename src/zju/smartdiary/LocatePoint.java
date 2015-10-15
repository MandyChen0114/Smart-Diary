package zju.smartdiary;

import java.math.BigDecimal;

public class LocatePoint {
	private double geoLat;
	private double geoLng;
	
	public LocatePoint(double geoLat,double geoLng){
		this.geoLat=geoLat;
		this.geoLng=geoLng;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(geoLat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(geoLng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) { 
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocatePoint other = (LocatePoint) obj;
		
//		BigDecimal bLat = new BigDecimal(other.geoLat);  
//		double ogeoLat = bLat.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
//		BigDecimal bLng = new BigDecimal(other.geoLng);  
//		double ogeoLng = bLng.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		
		if (Double.doubleToLongBits(geoLat) != Double
				.doubleToLongBits(other.geoLat))
			return false;
		if (Double.doubleToLongBits(geoLng) != Double
				.doubleToLongBits(other.geoLng))
			return false;
		return true;
	}	

}
