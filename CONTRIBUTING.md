# Contribution Guidelines of Earny

### :speech_balloon: Help or support needed?

If you want support you can contact me via telegram: [Telegram](https://telegram.me/deeprobin/)

### :bug: Bug reporting

Before reporting a bug or issue, please make sure that the issue is actually being caused by or related to Earny.

Bugs or issues should be reported using the [GitHub Issues tab](https://github.com/DeepRobin/earny/issues).

### :pencil: Contribution
#### Pull Requests
If you make any changes or improvements to the plugin which you think would be beneficial to others, please consider making a pull request to merge your changes back into the upstream project. (especially if your changes are bug fixes!)

Earny loosely follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Generally, try to copy the style of code found in the class you're editing. 

If you're considering submitting a substantial pull request, please open an issue so we can discuss the change before starting work on the contribution. Most pull requests are happily accepted, but larger changes may have an inpact on the maintainability of the project, and require more consideration. 

#### Project Layout
The project is split up into a few seperate modules.

* **Core** - The core module is the :hearth: of the project. - Each other module depends on him.
* **Bukkit, BungeeCord, Sponge, Forge, Nukkit & Velocity** - Each use the common module to implement plugins on the respective server platforms.
* **Universal** Universal is the module, which compiles each other module to one .jar file.
