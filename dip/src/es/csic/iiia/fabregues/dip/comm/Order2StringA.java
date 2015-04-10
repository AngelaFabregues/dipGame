package es.csic.iiia.fabregues.dip.comm;

import java.util.List;
import java.util.Vector;

import es.csic.iiia.fabregues.dip.orders.BLDOrder;
import es.csic.iiia.fabregues.dip.orders.DSBOrder;
import es.csic.iiia.fabregues.dip.orders.HLDOrder;
import es.csic.iiia.fabregues.dip.orders.MTOOrder;
import es.csic.iiia.fabregues.dip.orders.Order;
import es.csic.iiia.fabregues.dip.orders.REMOrder;
import es.csic.iiia.fabregues.dip.orders.RTOOrder;
import es.csic.iiia.fabregues.dip.orders.SUPMTOOrder;
import es.csic.iiia.fabregues.dip.orders.SUPOrder;
import es.csic.iiia.fabregues.dip.orders.WVEOrder;

/**
 * Writes an order using String[] notation.
 * 
 * @author Angela Fabregues, IIIA-CSIC, fabregues@iiia.csic.es
 */
public class Order2StringA {

	public static String[] getOrderResult(Order order){
		List<String> resultMsg = new Vector<String>();
		resultMsg.add("(");
		for(String str: getOrderMsg(order)){
			resultMsg.add(str);
		}
		resultMsg.add(")");
		resultMsg.add("(");
		String result = order.getResult();
		for(String ele: result.split(" ")){
			resultMsg.add(ele);
		}
		
		resultMsg.add(")");
		return resultMsg.toArray(new String[resultMsg.size()]);
	}
	
	public static String[] getOrderMsg(Order o) {
		String[] unitStrArr;
		String[] orderMsg;
		if(o instanceof BLDOrder){
			BLDOrder order = (BLDOrder) o;
			unitStrArr = ParseService.toUnitOrderStringArr(order.getLocation(),order.getPower());
			orderMsg = new String[1+unitStrArr.length];
			
			int i=0;
			for(String str: unitStrArr){
				orderMsg[i++] = str;
			}
			orderMsg[i++] = "BLD";
			return orderMsg;
		}else if(o instanceof DSBOrder){
			unitStrArr = ParseService.toUnitOrderStringArr(o.getLocation(), o.getPower());
			orderMsg = new String[1+unitStrArr.length];
			
			int i=0;
			for(String str: unitStrArr){
				orderMsg[i++] = str;
			}
			orderMsg[i++] = "DSB";
			return orderMsg;
		}else if(o instanceof HLDOrder){
			unitStrArr = ParseService.toUnitOrderStringArr(o.getLocation(), o.getPower());
			orderMsg = new String[1+unitStrArr.length];
			
			int i=0;
			for(String str: unitStrArr){
				orderMsg[i++] = str;
			}
			orderMsg[i++] = "HLD";
			return orderMsg;
		}else if(o instanceof MTOOrder){
			MTOOrder order = (MTOOrder) o;
			unitStrArr = ParseService.toUnitOrderStringArr(order.getLocation(), order.getPower());
			String[] destinationStrArr = ParseService.toRegionStringArr(order.getDestination());
			
			orderMsg = new String[1+unitStrArr.length+destinationStrArr.length];
			
			int i=0;
			for(String str: unitStrArr){
				orderMsg[i++] = str;
			}
			orderMsg[i++] = "MTO";
			for(String str: destinationStrArr){
				orderMsg[i++] = str;
			}

			return orderMsg;
		}else if(o instanceof REMOrder){
			unitStrArr = ParseService.toUnitOrderStringArr(o.getLocation(), o.getPower());
			orderMsg = new String[1+unitStrArr.length];
			
			int i=0;
			for(String str: unitStrArr){
				orderMsg[i++] = str;
			}
			orderMsg[i++] = "REM";
			return orderMsg;
		}else if(o instanceof RTOOrder){
			RTOOrder order = (RTOOrder) o;
			unitStrArr = ParseService.toUnitOrderStringArr(order.getLocation(), order.getPower());
			String[] destinationStrArr = ParseService.toRegionStringArr(order.getDestination());
			
			orderMsg = new String[1+unitStrArr.length+destinationStrArr.length];
			
			int i=0;
			for(String str: unitStrArr){
				orderMsg[i++] = str;
			}
			orderMsg[i++] = "RTO";
			for(String str: destinationStrArr){
				orderMsg[i++] = str;
			}

			return orderMsg;
		}else if(o instanceof SUPMTOOrder){
			SUPMTOOrder order = (SUPMTOOrder) o;
			String[] regionStrArr = ParseService.toUnitOrderStringArr(order.getLocation(), order.getPower());
			MTOOrder mtoOrder = order.getSupportedOrder();
			unitStrArr = ParseService.toUnitOrderStringArr(mtoOrder.getLocation(), mtoOrder.getPower());

			orderMsg = new String[regionStrArr.length+1+unitStrArr.length+1+1];
			
			int i=0;
			for(String str: regionStrArr){
				orderMsg[i++] = str;
			}
			
			orderMsg[i++] = "SUP";
			
			for(String str: unitStrArr){
				orderMsg[i++] = str;
			}
			orderMsg[i++] = "MTO";
			orderMsg[i++] = mtoOrder.getDestination().getProvince().getName();
			
			return orderMsg;
		}else if(o instanceof SUPOrder){
			SUPOrder order = (SUPOrder) o;
			unitStrArr = ParseService.toUnitOrderStringArr(order.getLocation(), order.getPower());
			Order supportedOrder = order.getSupportedOrder();
			String[] supportedRegionStrArr = ParseService.toUnitOrderStringArr(supportedOrder.getLocation(), supportedOrder.getPower());
			
			orderMsg = new String[1+unitStrArr.length+supportedRegionStrArr.length];
			
			int i=0;
			for(String str: unitStrArr){
				orderMsg[i++] = str;
			}
			
			orderMsg[i++] = "SUP";
			
			for(String str: supportedRegionStrArr){
				orderMsg[i++] = str;
			}
			
			return orderMsg;
		}else if(o instanceof WVEOrder){
			String[] order = new String[2];
			
			int i=0;
			order[i++] = o.getPower().getName();
			order[i++] = "WVE";
			return order;
		}
		return null;
	}
}
