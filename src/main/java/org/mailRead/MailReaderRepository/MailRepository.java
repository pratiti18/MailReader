package org.mailRead.MailReaderRepository;

import java.util.List;

import org.mailRead.MailReaderModel.EmailData;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<EmailData, Long>{
	public EmailData findBySubject(String name);
}
