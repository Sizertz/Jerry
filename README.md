# Jerry
Task automation and bug workarounds for TW:WH map makers

## Features

### Automated Building Linking
Entities can be linked to a building. They can then be moved and scaled relative to their parent building inside Terry and they will disappear in-game if the parent building gets destroyed.<br/>
This is typically useful for making custom destructible buildings.<br/>
This can be done by writing the link 

#### Step-by-step:
* Download [Jerry-Building-Linker.jar](https://github.com/Sizertz/Jerry/raw/master/Jerry/dist/Jerry-Building-Linker.jar)
* Open your Terry project
* Name the buildings that you want to become parents "parent" in the "Scene Hierarchy"
* Bundle your parents with their future children 
  * either by putting the parent and children in a separate layer
  * or by grouping the parent with its children
* 
