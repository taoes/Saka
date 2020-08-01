package com.zhoutao123.framework.saka.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 元方法集合
 *
 * @author zhoutao123
 */
public class MetaMethodArray {

  private static List<MetaMethod> metaMethods = null;

  public static synchronized void add(MetaMethod metaMethod) {
    if (metaMethods == null) {
      metaMethods = new ArrayList<MetaMethod>();
    }
    metaMethods.add(metaMethod);
  }

  /** 当前方法集合的size */
  public static int size() {
    return metaMethods.size();
  }

  /** 当前的方法集合 */
  public static boolean isEmpty() {
    return size() == 0;
  }

  public static List<MetaMethod> getMetaMethods() {
    return metaMethods;
  }

  /** 方法按照Order进行排序 */
  public static void sort() {
    if (metaMethods != null) {
      metaMethods.sort(
          new Comparator<MetaMethod>() {
            @Override
            public int compare(MetaMethod o1, MetaMethod o2) {
              if (o1.getOrder() == o2.getOrder()) {
                return 0;
              } else {
                return o1.getOrder() > o2.getOrder() ? 1 : -1;
              }
            }
          });
    }
  }
}
