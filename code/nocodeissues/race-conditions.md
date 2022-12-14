Race conditions can occur whenever there is a chance that
two operations will access a single row
and then make different changes to it (for example, update/delete).
Thus, in the spec items project, the following functions from
the `SpecItemService` can be potentially affected by various
race conditions:
1. `deleteSpecItemById`
2. `saveTags` (for example, a lost update)
3. `saveDocuments` (for example, when there was an addition of a tag in between)

Possible solutions:
1. Optimistic locking (mainly via the `@Version` annotation)
2. Pessimistic locking

Furthermore, to keep the state consistent, one can resort to transactions. They are used to combine more than one write on a database as a single atomic operation. The transaction can be added on a class or a method level. This is done by the use of the `@Transactional` keyword. Creating a transaction tells the system: Either you commit the whole operation or you do nothing.
A candidate for the `Transactional` method is `deleteSpecItemById`
because there can be more than one delete operation. If it fails in between, the system might become inconsistent.

Concerning reading the data (no updates),
the default isolation level of PSQL is read committed,
which does not allow any dirty reads. According to the research,
increasing the isolation level should be thoroughly considered
as it affects performance heavily.
