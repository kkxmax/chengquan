//
//  HotTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 10/24/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HotTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UILabel *lblContent;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollThumb;
@property (weak, nonatomic) IBOutlet UILabel *lblRead;
@property (weak, nonatomic) IBOutlet UILabel *lblEval;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;

@end
