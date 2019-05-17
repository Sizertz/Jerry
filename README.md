# Jerry
Task automation and bug workarounds for TW:WH map makers

## Automated Building Linking
Entities can be linked to a building. They can then be moved and scaled relative to their parent building inside Terry and they will disappear in-game if the parent building gets destroyed.

This is typically useful for making custom destructible buildings out of existing assets.

This can be done by writing the link by hand in your .layer files provided that the parent building is at scale (1,1,1), position (0,0,0) and rotation (0,0,0).

With Jerry, parents can have any position and rotation, though some constraints on scale remain due to limitations of Terry's way of storing transformations.

#### Step-by-step:
In short:
1. Name the buildings that you want to become parents "parent" in the "Scene Hierarchy".
2. Put each parent inside their own separate (logical) layer with everything you want to link them to.
3. Run Jerry's linker tool (see below).
4. Re-open the map in Terry.
5. Profit!

N.B. The only constraint you should follow is that the parent's 3 scale values should be equal (unless you know what you are doing, see below).

### How to run Jerry's linker tool
**Requirements:**
a recent-ish version of [Java](https://www.java.com/en/download/)

**The Windows ~~noob~~- .exe way**
1. Download [jerry-linker.exe](https://github.com/Sizertz/Jerry/raw/master/Jerry/dist/jerry-linker.exe)
2. Place jerry-linker.exe in your Terry project folder
3. Run jerry-linker.exe
(If your version of Java is too old/badly installed, you should be directed to the Java download page)
  
**The .jar way** (for people who like command line)
Download [jerry-linker.jar](https://github.com/Sizertz/Jerry/raw/master/Jerry/dist/jerry-linker.jar).

You can use it just like the .exe (provided you've installed Java well enough to be able to run the jar by double-clicking it).

If you've added Java to your environment variables, you can run it through command line. The basic syntax is:
```
java -jar jerry-linker.jar [input]
```
* if `[input]` is a .layer file, it will be processed
* if `[input]` is a folder, all the .layer files directly inside it will be processed
* if `[input]` is missing, all the .layer files in the working directory will be processed




