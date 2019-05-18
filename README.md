# Jerry
Task automation and bug workarounds for TW:WH map makers.

Written in Java and made to work with Terry, hence the name.

## Automated Building Linking
Entities can be linked to a building. They can then be moved and scaled relative to their parent building inside Terry and they will disappear in-game if the parent building gets destroyed.<br/>
This is typically useful for making custom destructible buildings out of existing assets.<br/>
This can be done by writing the link by hand in your .layer files provided that the parent building is at scale (1,1,1), position (0,0,0) and rotation (0,0,0).

With Jerry, parents can have any position and rotation, though some constraints on scale remain due to limitations of Terry's way of storing transformations.

#### Step-by-step:
In short:
1. Name the buildings that you want to become parents "parent" in the "Scene Hierarchy".
2. Put each parent inside their *own separate (logical) layer* with everything you want to link them to.
3. Run Jerry's linker tool (see below).
4. Re-open the map in Terry.
5. Profit!

**N.B.**
* The only constraint you should follow is that the parent's 3 scale values should be equal before you link it (unless you know what you are doing, see below).
* There is no need to place the parent at (0,0,0) like when you do the link by hand. Jerry does the necessary math.
* The "parent" name is removed from the parent building. 
* If you want to link other assets as children to the building, just re-name it "parent", place the future children in the same layer and re-run Jerry.
* Placing the parent and its future children at the root of the file layer is currently un-supported. You need to create a layer for each parent.
* Layers can't be children of a building (Terry crashes) but (nested) groups and other parent buildings can.
* Jerry creates a backup copy of each layer file before it overwrites it. It'll be right next to the original and named è `yourTerryProject.fileLayerID.layer.backup`. The backup is overwritten every time without warning, so if things don't work, don't forget to restore the backup before you try re-running Jerry.

### How to run Jerry's linker tool
**Requirements:**
a recent-ish version of [Java](https://www.java.com/en/download/)

**The Windows ~~noob~~- .exe way**
1. Download [jerry-linker.exe](https://github.com/Sizertz/Jerry/raw/master/Jerry/dist/jerry-linker.exe)
2. Place jerry-linker.exe in your Terry project folder
3. Run jerry-linker.exe
(If your version of Java is too old/badly installed, you should be directed to the Java download page)
  
**The .jar way** (for people who like command line)<br/>
Download [jerry-linker.jar](https://github.com/Sizertz/Jerry/raw/master/Jerry/dist/jerry-linker.jar).

You can use it just like the .exe (provided you've installed Java well enough to be able to run the jar by double-clicking it).

If you've added Java to your environment variables, you can run it through command line. The basic syntax is:
```
java -jar jerry-linker.jar [input]
```
* if `[input]` is a .layer file, it will be processed
* if `[input]` is a folder, all the .layer files directly inside it will be processed
* if `[input]` is missing, all the .layer files in the working directory will be processed

## Notes on scaling parent buildings
Scaling is a little clumsy in Terry.<br>
This is because scaling is stored as only three values, which mathematically limits what can be done.<br>

Scaling a parent building doesn't work like scaling a standard Terry group.<br>
Groups are seemingly ungrouped before they are exported for the in-game version of the map. As a result, scaling groups inside Terry is not recommended as you cannot see the final look in Terry.<br>
On the other hand, scaling a parent building will scale all its children, both in Terry and in-game.

This is great. It means that things don't get thrown out of alignment and also that you can use building linking to achieve scaling in any direction.

However, the fact that the parent's scaling is always applied to its children means that there are mathematical constraints on how you can scale a parent and rotate its future children before you link them.<br>
In short:
* If the parent is scaled equally in all three directions, then you can scale and rotate the future children any way you like
* If the parent is scaled the same in only two directions, then all the future children need to be rotated in a way such that one of their local axes is aligned with the third direction (the one along which the parent is scaled differently).<br>
  e.g. if the parent is only scaled vertically, then all its future children need to have one of their axes be vertical too (pointing up or down doesn't matter), but can be otherwise rotated and scaled any way you like.
* If the parent is scaled differently in all three directions, then all three axes of each future child need to line up with those of the parent. In other words, relative to the parent, future children need to be rotated by multiples of 90°.

**N.B**
* If you do not meet these conditions, there is mathematically no way for children and parent to look the same before and after they are linked. Jerry will make sure that the parent looks the same and that the child has the correct position but its scaling will necessarily be off.
* After you have linked child and parent, you can of course freely transform them.

___
Next on the workbench:
* a workaround for the outfield height bug
* a GUI

