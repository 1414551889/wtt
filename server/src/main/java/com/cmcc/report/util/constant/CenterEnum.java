/**
 * @Title: ConfigItemStatusEnum.java
 * @Package com.suninfo.util.constant
 * Company:suninfo
 * @author yhf
 * @date 2016年3月29日 下午5:31:51
 */

package com.cmcc.report.util.constant;

import org.apache.commons.lang.enums.ValuedEnum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
  *
  * @ClassName: CenterEnum
  * @author
  * @date
  */
public class CenterEnum extends ValuedEnum{

	public static final Integer  yiliao= 1;
	public static final Integer jiaoyu = 2;
	public static final Integer jiaotong = 3;
	public static final Integer  guanxin= 4;
	public static final Integer yunhuaoa = 5;
	public static final Integer anquan = 6;
	public static final Integer zhiliang = 7;

	public static final CenterEnum YILIAO = new CenterEnum("医疗行业中心", yiliao);

	public static final CenterEnum JIAOYU = new CenterEnum("教育行业中心", jiaoyu);

	public static final CenterEnum JIAOTONG = new CenterEnum("交通行业中心", jiaotong);
	public static final CenterEnum GUANXIN = new CenterEnum("管理信息化中心", guanxin);

	public static final CenterEnum YUNHUAOA = new CenterEnum("云化OA项目组", yunhuaoa);

	public static final CenterEnum ANQUAN = new CenterEnum("安全项目中心", anquan);
	public static final CenterEnum ZHILIANG = new CenterEnum("质量管理中心", zhiliang);


	protected CenterEnum(String name, int value) {
		super(name, value);
	}

	public static CenterEnum getEnum(String type) {
		return (CenterEnum) getEnum(CenterEnum.class, type);
	}

	public static CenterEnum getEnum(int type) {
		return (CenterEnum) getEnum(CenterEnum.class, type);
	}

	public static Map UserGenderEnum() {
		return getEnumMap(CenterEnum.class);
	}

	public static List getEnumList() {
		return getEnumList(CenterEnum.class);
	}

	public static Iterator iterator() {
		return iterator(CenterEnum.class);
	}
}
