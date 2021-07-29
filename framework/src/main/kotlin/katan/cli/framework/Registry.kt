package katan.cli.framework

/**
 * Commands registry used by [Parser] to found known commands.
 *
 * @author Natan Vieira
 */
class Registry(vararg commands: Command) {

    private val commands: MutableSet<Command> = hashSetOf()

    init {
        this.commands.addAll(commands.toSet())
    }

    /**
     * Registers all the given commands.
     * @param commands commands to be registered.
     */
    fun register(vararg commands: Command) {
        this.commands.addAll(commands)
    }

    /**
     * Returns a registered [Command] with the specified name (case insensitive)
     * or `null` if the command is not registered or doesn't exist.
     * @param name the command name.
     */
    fun search(name: String): Command? {
        return commands.find { it.name.equals(name, true) }
    }

}