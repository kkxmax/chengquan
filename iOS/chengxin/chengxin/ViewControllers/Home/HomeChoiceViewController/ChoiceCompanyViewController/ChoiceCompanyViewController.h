//
//  HomeChoiceAllCityViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ChoiceCompanyViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>
{
}
@property (nonatomic) NSInteger companyIndex;
@property (nonatomic, strong) IBOutlet UITableView* choiceCompanyTableView;

@end
