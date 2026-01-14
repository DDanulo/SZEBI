package com.example.server.Communication.repositories;

import com.example.server.Administration.model.User;
import com.example.server.Communication.objects.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c JOIN c.participants p WHERE p.id = :userId ORDER BY (SELECT MAX(m.timestamp) FROM c.messages m) DESC")
    List<Conversation> findByParticipantId(@Param("userId") UUID userId);

    @Query("SELECT c FROM Conversation c WHERE :user1 MEMBER OF c.participants AND :user2 MEMBER OF c.participants AND SIZE(c.participants) = 2")
    Optional<Conversation> findConversationBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);
}
