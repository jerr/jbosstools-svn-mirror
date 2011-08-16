package org.jboss.ide.eclipse.as.egit.internal.ui.util;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

public class CommandUtils {

	public static boolean executeCommand(String commandId, IStructuredSelection selection) throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException {
		Command command = getCommand(commandId);
		if (!command.isDefined()) {
			return false;
		}

		IHandlerService handlerService = (IHandlerService) 
				PlatformUI.getWorkbench().getService(IHandlerService.class);
		EvaluationContext context = createEvaluationContext(selection, handlerService);
		return doExecuteCommand(commandId, command, handlerService, context);
	}

	private static EvaluationContext createEvaluationContext(IStructuredSelection selection,
			IHandlerService handlerService) {
		EvaluationContext context = null;
		if (selection != null) {
			context = new EvaluationContext(
					handlerService.createContextSnapshot(false),
					selection.toList());
			context.addVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME, selection);
			context.removeVariable(ISources.ACTIVE_MENU_SELECTION_NAME);
		}
		return context;
	}

	private static boolean doExecuteCommand(String commandId, Command command, IHandlerService handlerService,
			EvaluationContext context) throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException {
			if (context != null) {
				handlerService.executeCommandInContext(
						new ParameterizedCommand(command, null), null, context);
			} else {
				handlerService.executeCommand(commandId, null);
			}
			return true;
	}

	public static Command getCommand(String commandId) {
		ICommandService commandService =
				(ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		return commandService.getCommand(commandId);
	}
}
