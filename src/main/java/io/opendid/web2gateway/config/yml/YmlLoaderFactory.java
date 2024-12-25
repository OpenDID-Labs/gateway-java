// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)
package io.opendid.web2gateway.config.yml;

import java.io.IOException;
import java.util.List;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class YmlLoaderFactory extends DefaultPropertySourceFactory {

  @NonNull
  @Override
  public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource)
      throws IOException {

    final List<PropertySource<?>> load = new YamlPropertySourceLoader()
        .load(resource.getResource().getFilename(), resource.getResource());

    if(load!=null && load.size()== 1){
      return load.get(0);
    }else{
      return super.createPropertySource(name,resource);
    }
  }

}
