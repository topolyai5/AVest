AVest
=====
Dependency and View injector

Annotations:

@Component
Mark a class for dependency management
```java
@Component
public class Trainer {}
```

@Configuration
Mark a class for configuration.
Search all @Vest in this class and registering for inject.

```java
@Configuration
public class Config {}
```

@EmbeddedLayout
Mark a field. This field inflate from a layout XML. It's a view and can inject a view from it. See @InjectView.

@EmbeddedLayout(R.layout.layout)
private LinearLayout layout;

@Inject
Inject a @Component.

@InjectView
Search a view in the layout.

value = the id of the view
layout = what is the layout, where find the view. Default is "".

@InjectView(value=R.id.view_id, layour="embeddedLayoutFieldName")
private View view;

@Layout
The laout of the Activity, Fragment, Dialog etc.

@PostConstruct
When the class initialization are finished  and the dependencies are injected AVest call this method.

@ScreenElement
Mark a class for enable to the @InjectView.

@Vest
Typically a third party class injection.

@Vest
private ThirdPartyClass createThirdpartyClass() {
	return new ThirdPartyClass(some, parameters);
}

Lifecycles:
* Create all components, Configurations and ScreenElements
* Create all @Vest from the configuration classes
* Fetch all system services
* resolve dependencies
* resolve views in activities and screen elements
* call @PostConstruct

