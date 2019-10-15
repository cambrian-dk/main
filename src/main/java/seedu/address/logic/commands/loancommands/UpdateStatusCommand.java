package seedu.address.logic.commands.loancommands;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.LoansManager;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.person.loan.Loan;
import seedu.address.model.person.loan.Status;
import seedu.address.model.person.loan.exceptions.LoanNotFoundException;

/**
 * Updates the status of a loan.
 */
public abstract class UpdateStatusCommand extends MultiLoanCommand {

    public UpdateStatusCommand(
            List<Index> targetPersonsIndices, List<Index> targetLoansIndices) throws CommandException {
        super(targetPersonsIndices, targetLoansIndices);
    }

    /**
     * Updates the statues of one or more existing loans to the given status.
     * @return A list of person-loan index pairs that could not be found among existing loans.
     */
    public List<PersonLoanIndexPair> updateStatuses(LoansManager loansManager, Status updatedStatus) {
        List<PersonLoanIndexPair> pairsNotFound = new ArrayList<PersonLoanIndexPair>();
        for (int i = 0; i < personLoanIndexPairs.size(); i++) {
            try {
                Person targetPerson = loansManager.getPersonsList().get(
                        personLoanIndexPairs.get(i).getPersonIndex().getZeroBased()
                );

                Loan targetLoan = targetPerson.getLoans().get(
                        personLoanIndexPairs.get(i).getLoanIndex().getZeroBased()
                );

                Loan updatedLoan = createUpdatedLoan(targetLoan, updatedStatus);

                loansManager.updateLoanStatus(targetPerson, targetLoan, updatedLoan);
            } catch (IndexOutOfBoundsException | PersonNotFoundException | LoanNotFoundException e) {
                pairsNotFound.add(personLoanIndexPairs.get(i));
            }
        }
        return pairsNotFound;
    }

    /**
     * Creates a copy of a loan, but with its status updated to the given status.
     * @return The loan with the updated status.
     */
    protected static Loan createUpdatedLoan(Loan loanToUpdate, Status updatedStatus) {
        assert loanToUpdate != null;
        return new Loan(
                loanToUpdate.getPerson(),
                loanToUpdate.getDirection(),
                loanToUpdate.getAmount(),
                loanToUpdate.getDate(),
                loanToUpdate.getDescription(),
                updatedStatus
        );
    }
}
