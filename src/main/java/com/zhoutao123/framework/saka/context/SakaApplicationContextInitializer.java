package com.zhoutao123.framework.saka.context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/** Saka Application初始化入口 */
public class SakaApplicationContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {
  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    applicationContext.addApplicationListener(
        (ApplicationListener<SakaInitializedEvent>)
            event ->
                SakaApplicationContextInitializer.this.onApplicationEvent(
                    applicationContext, event));
  }

  private void onApplicationEvent(
      ConfigurableApplicationContext applicationContext, SakaInitializedEvent event) {
    MutablePropertySources sources = applicationContext.getEnvironment().getPropertySources();
    PropertySource<?> source = sources.get("meta");
    if (source == null) {
      source = new MapPropertySource("meta", new HashMap<>());
      sources.addFirst(source);
    }
    Map<String, Object> source1 = (Map<String, Object>) source.getSource();
    source1.put("meta.method", event.getServer().getInstance());
  }
}
