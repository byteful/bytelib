# bytelib
A simple, modular Spigot library with useful utilities.
<br>This library was designed by and for byteful.

This library is still in early development, so it is not advised to use currently.

# Modules
## GUI
Example configurable GUI
```yml
gui:
  title: "test"
  layout:
    - "aaaaaaaaa"
    - "a       a"
    - "aaaaaaaaa"
  buttons:
    a:
      name: "&cTest"
      lore:
        - "&7Test"
      material: BARRIER
      action: "test-action" # optional
      action_meta: "some-argument-data" # optional
  ```
  
  The "test-action" action will be registered in the code, while the layout and button displays are configurable in YML.
