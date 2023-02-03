package com.example.service.api.impl;

import com.example.service.api.controller.PetApiDelegate;
import com.example.service.api.model.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PetApiDelegateImpl implements PetApiDelegate {
  @Override
  public ResponseEntity<Pet> addPet(Pet pet) {
    return ResponseEntity.ok(pet);
  }

}
