package com.inn.itrust.op.prediction;

/*
 * #%L
 * itrust-methods
 * %%
 * Copyright (C) 2014 INNOVA S.p.A
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.inn.itrust.model.pojo.TResource;



/**
 * Ideja je predikcija trusta. 
 * Moze biti interesantno u slucajevima kad nemamo dovoljno informacija o kvaliteti servisa 
 * ili o reputaciji servisa. (reputacija moze biti bazirana na povratnoj informaciji o
 * od korisnika, popularnosti, i sl.)
 * pogledam hidden markov model / da li predikcija u tom pravcu ima smisla
 * 
 * @author marko
 *
 */
public interface TrustPredictionManager {
	
	
	void predict(TResource tResource);

}
