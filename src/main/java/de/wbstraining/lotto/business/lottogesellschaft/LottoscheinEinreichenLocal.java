/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.business.lottogesellschaft;

import javax.ejb.Local;

import de.wbstraining.lotto.persistence.model.Lottoschein;

// persistiert den lottoschein

// persistiert für jedes datum, an dem der lottoschein
// bei einer ziehung teilnimmt, einen record in lottoscheinziehung

// precondition
// alle ziehungen, an denen der lottoschein teilnimmt, sind bereits angelegt

@Local
public interface LottoscheinEinreichenLocal {
	public void lottoscheinEinreichen(Lottoschein schein);
}
