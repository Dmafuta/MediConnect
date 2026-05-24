package com.mediconnect.appointment.repository;

import com.mediconnect.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAll(Pageable pageable);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByProviderId(Long providerId);

    List<Appointment> findByAppointmentDate(LocalDate date);

    List<Appointment> findByProviderIdAndAppointmentDate(Long providerId, LocalDate date);

    boolean existsByProviderIdAndAppointmentDateAndAppointmentTimeAndAppointmentStatusNotIn(
            Long providerId, LocalDate date, LocalTime time, List<String> excludedStatuses);

    long countByAppointmentDate(LocalDate date);

    @Query("SELECT a.appointmentStatus, COUNT(a) FROM Appointment a WHERE a.appointmentDate BETWEEN :from AND :to GROUP BY a.appointmentStatus")
    List<Object[]> countByStatusInRange(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
