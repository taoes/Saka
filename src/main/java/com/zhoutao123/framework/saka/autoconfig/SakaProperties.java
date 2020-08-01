package com.zhoutao123.framework.saka.autoconfig;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("saka")
public class SakaProperties {

  /** 是否启用 */
  private boolean enable = true;

  /** 扫描路径 */
  private String[] scanPath;

  /** Open Subscribe's Order execute,default value is close */
  private boolean sequenceExecute = false;
}
