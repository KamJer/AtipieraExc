package com.kamjer.AtiperaExc.service;

import com.kamjer.AtiperaExc.client.GitHubClient;
import com.kamjer.AtiperaExc.model.BranchDto;
import com.kamjer.AtiperaExc.model.RepositoryDto;
import com.kamjer.AtiperaExc.model.RepositoryResponse;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@Service
@AllArgsConstructor
public class GitHubService {

    private final GitHubClient gitHubClient;

    public List<RepositoryResponse> getGitHubRepository(String owner, String token) throws RestClientException {
        return getGitHubRepositoryToken(owner, Optional.of(token));
    }

    public List<RepositoryResponse> getGitHubRepository(String owner) throws RestClientException {
        return getGitHubRepositoryToken(owner, Optional.empty());
    }

    private List<RepositoryResponse> getGitHubRepositoryToken(String owner, Optional<String> token) throws RestClientException {
        return gitHubClient.getGitHubRepositories(token, owner)
                .stream()
                .filter( repositoryDto -> !repositoryDto.isFork())
                .map(repositoryDto -> new RepositoryResponse(repositoryDto, getBranchesForRepo(owner, repositoryDto.getName())))
                .toList();
    }

    private List<BranchDto> getBranchesForRepo(String owner, String repoName) {
        return gitHubClient.getGitHubBranches(owner, repoName);
    }
}

