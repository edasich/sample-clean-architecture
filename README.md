# Domain Driven Design + Clean Architecture + MVI

The application is juts tries to collect the current location of user with fixed intervals
and then fetches and caches all vendors based on the location,
then the application queries the vendors based on radius in local database to find nearby vendors and show them on the map.
The user can bookmark the vendors on map to see it later whenever needed.

## The Layers

### UI Modules
As Uncle Bob saied that, the arcitecture of UI layer can be anything as long as we seprate the business logic from our UI.
This layer considered to be Delivery Mechanism Layer and in sample I chose the MVI.

This layer is responsible to work with Interactors and map the domain models into UI models by using mappers. 

### Interactor Modules

It is seprated into 3 layers.

- Service : All the interactors plased here but only the interfaces.
- Service Internal : All the implementation of the interactors placed here.
- Service DI : Because these services are pure kotlin module, the DI layer come to the scene to create graph for dagger.

### Domain Modules

Modelized the business logics with having Entity objects, Value objects, Aggregate roots, and policies.

### Data Modules

Dealing for managing data with local and remote data sources and mapped the data into domain models by using mappers.

### Device Modules

Any logic that can be part of Android Device or Framework and can be worked with interactors are placed here.
Like locattion-device module. Only Interactors can work with device modules by inversion of control.

### Base Modules

They are shared some reusable modules on each specific layer.


