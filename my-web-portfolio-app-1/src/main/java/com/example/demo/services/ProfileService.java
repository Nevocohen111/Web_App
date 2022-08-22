package com.example.demo.services;

import com.example.demo.model.Activity;
import com.example.demo.model.Profile;
import com.example.demo.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public void updateProfile(Integer id, String name,String userName, String lastName, String email, String hobbies, String workPlace, String phone) {
        Profile profile = profileRepository.findById(id).get();
        if (profile.getId().equals(id)) {
            if (!name.isEmpty()) {
                profile.setName(name);
            }
            if(!userName.isEmpty()) {
                profile.setUserName(userName);
            }
            if (!lastName.isEmpty()) {
                profile.setLastName(lastName);
            }
            if (!email.isEmpty()) {
                profile.setEmail(email);
            }
            if (!hobbies.isEmpty()) {
                profile.setHobbies(hobbies);
            }
            if (!workPlace.isEmpty()) {
                profile.setWorkPlace(workPlace);
            }
            if (!phone.isEmpty()) {
                profile.setPhone(phone);
            }
        }
        profileRepository.save(profile);
    }

    //createname
    public void setName(String name) {
        Profile profile = profileRepository.findById(1).get();
        profile.setName(name);
        profileRepository.save(profile);
    }



    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public List<Profile> getGuestImgUrl() {
        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profile.setImgURL("https://www.w3schools.com/w3images/avatar2.png");
        profiles.add(profile);
        return profiles;
    }

    public List<Profile> getNevoImgUrl() {
        List<Profile> profiles = new ArrayList<>();
        Profile profile = new Profile();
        profile.setImgURL("images/276057892_383256297002672_7942043668246412422_n.jpg");
        profiles.add(profile);
        return profiles;
    }

    public List<Profile> getAllCurrentUserProfiles(String name) {
        return profileRepository.findAllByName(name);
    }

    public Profile getProfile(String name) {
        return profileRepository.findByName(name);
    }

    public void createProfile(Profile profile,String name,String userName, String lastName, String email, String hobbies, String workPlace, String phone) {
        //if profile is new, create new profile only 1 time in db then update it for further use
        //else update by it's id
        if (profileRepository.findByName(name) == null) {
            profile.setImgURL("https://www.w3schools.com/w3images/avatar2.png");
            profile.setName(name);
            profile.setLastName(lastName);
            profile.setEmail(email);
            profile.setHobbies(hobbies);
            profile.setWorkPlace(workPlace);
            profile.setPhone(phone);
            profileRepository.save(profile);
        } else {
            for(Profile p : profileRepository.findAllByName(name)) {
                updateProfile(p.getId(), name,userName, lastName, email, hobbies, workPlace, phone);
            }
        }

    }

    public void deleteById(int userId) {
        Profile profile = profileRepository.findById(userId + 1).get();
        profileRepository.delete(profile);
    }

    public void deleteByProfileName(String name) {
        List<Profile> list = profileRepository.findAllByName(name);
        profileRepository.deleteAll(list);
    }

    public List<Profile> findById(int userId) {
        return profileRepository.findProfileById(userId);
    }

    public boolean isProfileExistsById(int userId) {
        return profileRepository.existsById(userId + 1);
    }

    public List<Profile> findProfileByName(String name) {
        return profileRepository.findAllByName(name);
    }





   /* public boolean createImage(MultipartFile file) throws IOException, InterruptedException {
        String path = "target/classes/static/images/";
        Files.write(Paths.get(path + file.getOriginalFilename()), file.getBytes(), StandardOpenOption.CREATE);
        String fileName = "images/" + file.getOriginalFilename();







   /* public boolean createImage(MultipartFile file) throws IOException, InterruptedException {
        String path = "target/classes/static/images/";
        Files.write(Paths.get(path + file.getOriginalFilename()), file.getBytes(), StandardOpenOption.CREATE);
        String fileName = "images/" + file.getOriginalFilename();
        profileRepository.createImage(250, fileName);
        return true;
    }*/
  /*  public boolean createImage(MultipartFile file) throws IOException, InterruptedException {
        //create a path to the directory storage on amazon aws
        String fileName = file.getOriginalFilename();
        String filePath = "https://s3.amazonaws.com/profile-pictures/" + fileName;
        InputStream inputStream = file.getInputStream();
        BufferedImage image = ImageIO.read(inputStream);
        File outputFile = new File("target/classes/static/images/" + fileName);
        ImageIO.write(image, "jpg", outputFile);
        Path path = Paths.get("target/classes/static/images/" + fileName);
        Files.copy(path, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        profileRepository.createImage(250, filePath);
        return true;
    }*/

}
